package com.microservice.auth_service.microservice_auth_service.service;

import com.microservice.auth_service.microservice_auth_service.controller.ResourceNotFoundException;
import com.microservice.auth_service.microservice_auth_service.dto.*;
import com.microservice.auth_service.microservice_auth_service.model.PreguntaSeguridad;
import com.microservice.auth_service.microservice_auth_service.model.RespuestaSeguridad;
import com.microservice.auth_service.microservice_auth_service.model.Usuario;
import com.microservice.auth_service.microservice_auth_service.repository.PreguntaSeguridadRepository;
import com.microservice.auth_service.microservice_auth_service.repository.RespuestaSeguridadRepository;
import com.microservice.auth_service.microservice_auth_service.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class PasswordRecoveryService {

    private final UsuarioRepository usuarioRepo;
    private final PreguntaSeguridadRepository preguntaRepo;
    private final RespuestaSeguridadRepository respuestaRepo;
    private final PasswordEncoder passwordEncoder;
    private final PasswordRecoveryTokenService tokenService;

    public PasswordRecoveryService(
            UsuarioRepository usuarioRepo,
            PreguntaSeguridadRepository preguntaRepo,
            RespuestaSeguridadRepository respuestaRepo,
            PasswordEncoder passwordEncoder,
            PasswordRecoveryTokenService tokenService
    ) {
        this.usuarioRepo = usuarioRepo;
        this.preguntaRepo = preguntaRepo;
        this.respuestaRepo = respuestaRepo;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    // Paso 1: obtener preguntas del usuario por identificador
    @Transactional(readOnly = true)
    public RecoveryQuestionsResponse getQuestions(String identificador) {
        Usuario user = findUserByIdentificador(identificador);

        List<RespuestaSeguridad> respuestas = respuestaRepo.findByUsuarioId(user.getId());
        if (respuestas == null || respuestas.size() != 3) {
            throw new ResourceNotFoundException("El usuario no tiene 3 preguntas configuradas");
        }

        List<PreguntaSeguridadDto> preguntas = respuestas.stream().map(r -> {
            PreguntaSeguridad p = preguntaRepo.findById(r.getPreguntaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pregunta inválida: " + r.getPreguntaId()));
            return new PreguntaSeguridadDto(p.getId(), p.getTexto());
        }).toList();

        return new RecoveryQuestionsResponse(user.getId(), preguntas);
    }

    // Paso 2: verificar respuestas → devolver token
    public String verifyAnswers(VerifyRecoveryRequest req) {

        if (req.userId() == null) throw new IllegalArgumentException("userId es requerido");
        if (req.items() == null || req.items().size() != 3) {
            throw new IllegalArgumentException("Debes responder exactamente 3 preguntas");
        }

        usuarioRepo.findById(req.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<RespuestaSeguridad> guardadas = respuestaRepo.findByUsuarioId(req.userId());
        if (guardadas == null || guardadas.size() != 3) {
            throw new ResourceNotFoundException("El usuario no tiene 3 preguntas configuradas");
        }

        Map<Integer, String> hashPorPregunta = new HashMap<>();
        for (var r : guardadas) hashPorPregunta.put(r.getPreguntaId(), r.getRespuestaHash());

        Set<Integer> ids = new HashSet<>();
        for (var item : req.items()) {
            if (item.preguntaId() == null) throw new IllegalArgumentException("preguntaId es requerido");
            if (!ids.add(item.preguntaId())) throw new IllegalArgumentException("No puedes repetir preguntas");
            if (item.respuesta() == null || item.respuesta().isBlank()) {
                throw new IllegalArgumentException("La respuesta no puede estar vacía");
            }

            String storedHash = hashPorPregunta.get(item.preguntaId());
            if (storedHash == null) throw new IllegalArgumentException("Pregunta no configurada para este usuario");

            String normalized = normalize(item.respuesta());
            if (!passwordEncoder.matches(normalized, storedHash)) {
                throw new IllegalArgumentException("Respuestas incorrectas");
            }
        }

        return tokenService.create(req.userId());
    }

    // Paso 3: reset password con token
    public void resetPassword(ResetPasswordRequest req) {

        if (req.token() == null || req.token().isBlank()) throw new IllegalArgumentException("token es requerido");
        if (req.newPassword() == null || req.newPassword().isBlank()) throw new IllegalArgumentException("newPassword es requerida");
        if (req.newPassword().length() < 8) throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");

        Long userId = tokenService.consume(req.token());

        Usuario user = usuarioRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        user.setClave(passwordEncoder.encode(req.newPassword()));
        usuarioRepo.save(user);
    }

    // Helpers
    private Usuario findUserByIdentificador(String identificador) {
        if (identificador == null || identificador.isBlank()) {
            throw new IllegalArgumentException("identificador es requerido");
        }
        return usuarioRepo.findByCorreoIgnoreCase(identificador)
                .or(() -> usuarioRepo.findByNombreIgnoreCase(identificador))
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    private String normalize(String s) {
        return s == null ? "" : s.trim().toLowerCase(Locale.ROOT);
    }
}
