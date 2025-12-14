package com.microservice.auth_service.microservice_auth_service.service;

import com.microservice.auth_service.microservice_auth_service.dto.SecurityQuestionDto;
import com.microservice.auth_service.microservice_auth_service.dto.SetupSecurityQuestionsRequest;
import com.microservice.auth_service.microservice_auth_service.model.RespuestaSeguridad;
import com.microservice.auth_service.microservice_auth_service.repository.PreguntaSeguridadRepository;
import com.microservice.auth_service.microservice_auth_service.repository.RespuestaSeguridadRepository;
import com.microservice.auth_service.microservice_auth_service.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@Transactional
public class SecurityQuestionsService {

    private final PreguntaSeguridadRepository preguntaRepo;
    private final RespuestaSeguridadRepository respuestaRepo;
    private final UsuarioRepository usuarioRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public SecurityQuestionsService(
            PreguntaSeguridadRepository preguntaRepo,
            RespuestaSeguridadRepository respuestaRepo,
            UsuarioRepository usuarioRepo,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.preguntaRepo = preguntaRepo;
        this.respuestaRepo = respuestaRepo;
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // =====================================================
    // LISTAR PREGUNTAS ACTIVAS
    // =====================================================
    @Transactional(readOnly = true)
    public List<SecurityQuestionDto> listarActivas() {
        return preguntaRepo.findByActivaTrue()
                .stream()
                .map(p -> new SecurityQuestionDto(p.getId(), p.getTexto()))
                .toList();
    }

    // =====================================================
    // SETUP PREGUNTAS DE SEGURIDAD (REGISTRO)
    // =====================================================
    public void setupPreguntas(SetupSecurityQuestionsRequest req) {

        // Validaciones básicas
        if (req.getUserId() == null) {
            throw new IllegalArgumentException("userId es requerido");
        }

        if (req.getItems() == null || req.getItems().size() != 3) {
            throw new IllegalArgumentException("Debes elegir exactamente 3 preguntas");
        }

        // Verificar usuario
        usuarioRepo.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar preguntas únicas
        Set<Integer> ids = new HashSet<>();
        req.getItems().forEach(item -> {
            if (!ids.add(item.getPreguntaId())) {
                throw new IllegalArgumentException("No puedes repetir preguntas");
            }
        });

        // Validar que las preguntas existan
        ids.forEach(pid ->
                preguntaRepo.findById(pid)
                        .orElseThrow(() -> new RuntimeException("Pregunta inválida: " + pid))
        );

        // Si ya tenía preguntas → las eliminamos
        if (respuestaRepo.existsByUsuarioId(req.getUserId())) {
            respuestaRepo.deleteByUsuarioId(req.getUserId());
        }

        // Guardar respuestas hasheadas
        req.getItems().forEach(item -> {

            if (item.getRespuesta() == null || item.getRespuesta().isBlank()) {
                throw new IllegalArgumentException("La respuesta no puede estar vacía");
            }

            String normalized = normalize(item.getRespuesta());
            String hash = passwordEncoder.encode(normalized);

            RespuestaSeguridad r = new RespuestaSeguridad();
            r.setUsuarioId(req.getUserId());
            r.setPreguntaId(item.getPreguntaId());
            r.setRespuestaHash(hash);

            respuestaRepo.save(r);
        });
    }

    // =====================================================
    // NORMALIZADOR (MISMO QUE EN VERIFY)
    // =====================================================
    private String normalize(String s) {
        return s == null ? "" : s.trim().toLowerCase(Locale.ROOT);
    }
}
