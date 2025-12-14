package com.microservice.auth_service.microservice_auth_service.services;

import com.microservice.auth_service.microservice_auth_service.dto.SetupSecurityQuestionsRequest;
import com.microservice.auth_service.microservice_auth_service.model.PreguntaSeguridad;
import com.microservice.auth_service.microservice_auth_service.model.Usuario;
import com.microservice.auth_service.microservice_auth_service.repository.PreguntaSeguridadRepository;
import com.microservice.auth_service.microservice_auth_service.repository.RespuestaSeguridadRepository;
import com.microservice.auth_service.microservice_auth_service.repository.UsuarioRepository;
import com.microservice.auth_service.microservice_auth_service.service.SecurityQuestionsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityQuestionsServiceTest {

    @Mock PreguntaSeguridadRepository preguntaRepo;
    @Mock RespuestaSeguridadRepository respuestaRepo;
    @Mock UsuarioRepository usuarioRepo;
    @Mock BCryptPasswordEncoder passwordEncoder; // ✅ para evitar NPE

    @InjectMocks SecurityQuestionsService service;

    @Test
    void listarActivas_filtraSoloActivas() {
        PreguntaSeguridad p1 = new PreguntaSeguridad();
        p1.setId(1);
        p1.setTexto("Activa");
        p1.setActiva(true);

        PreguntaSeguridad p2 = new PreguntaSeguridad();
        p2.setId(2);
        p2.setTexto("Inactiva");
        p2.setActiva(false);

        // ✅ el service usa findByActivaTrue(), no findAll()
        when(preguntaRepo.findByActivaTrue()).thenReturn(List.of(p1));

        var result = service.listarActivas();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).id());
        assertEquals("Activa", result.get(0).texto());
    }

    @Test
    void setupPreguntas_siNoSon3_devuelve400() {
        SetupSecurityQuestionsRequest req = new SetupSecurityQuestionsRequest();
        req.setUserId(1L);

        SetupSecurityQuestionsRequest.Item i1 = new SetupSecurityQuestionsRequest.Item();
        i1.setPreguntaId(1);
        i1.setRespuesta("uno");

        req.setItems(List.of(i1)); // solo 1

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.setupPreguntas(req));

        assertTrue(ex.getMessage().toLowerCase().contains("exactamente 3"));
        verifyNoInteractions(usuarioRepo, preguntaRepo, respuestaRepo);
    }

    @Test
    void setupPreguntas_usuarioNoExiste_devuelve404() {
        SetupSecurityQuestionsRequest req = buildValidRequest();

        when(usuarioRepo.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.setupPreguntas(req));

        assertTrue(ex.getMessage().toLowerCase().contains("usuario"));
        verify(usuarioRepo).findById(1L);
        verifyNoInteractions(preguntaRepo, respuestaRepo);
    }

    @Test
    void setupPreguntas_preguntaInvalida_devuelve404() {
        SetupSecurityQuestionsRequest req = buildValidRequest();

        when(usuarioRepo.findById(1L)).thenReturn(Optional.of(new Usuario()));
        when(preguntaRepo.findById(1)).thenReturn(Optional.of(mockPregunta(1)));
        when(preguntaRepo.findById(2)).thenReturn(Optional.empty()); // inválida

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.setupPreguntas(req));

        assertTrue(ex.getMessage().toLowerCase().contains("inválida") ||
                ex.getMessage().toLowerCase().contains("invalida"));
        verify(respuestaRepo, never()).save(any());
    }

    @Test
    void setupPreguntas_ok_guarda3Respuestas() {
        SetupSecurityQuestionsRequest req = buildValidRequest();

        when(usuarioRepo.findById(1L)).thenReturn(Optional.of(new Usuario()));
        when(preguntaRepo.findById(1)).thenReturn(Optional.of(mockPregunta(1)));
        when(preguntaRepo.findById(2)).thenReturn(Optional.of(mockPregunta(2)));
        when(preguntaRepo.findById(3)).thenReturn(Optional.of(mockPregunta(3)));

        // ✅ evita NPE y permite que se guarden hashes
        when(passwordEncoder.encode(anyString())).thenReturn("HASH");

        service.setupPreguntas(req);

        ArgumentCaptor<com.microservice.auth_service.microservice_auth_service.model.RespuestaSeguridad> cap =
                ArgumentCaptor.forClass(com.microservice.auth_service.microservice_auth_service.model.RespuestaSeguridad.class);

        verify(respuestaRepo, times(3)).save(cap.capture());

        var saved = cap.getAllValues();
        assertEquals(3, saved.size());
        saved.forEach(r -> {
            assertEquals(1L, r.getUsuarioId());
            assertNotNull(r.getPreguntaId());
            assertNotNull(r.getRespuestaHash());
            assertFalse(r.getRespuestaHash().isBlank());
        });
    }

    private SetupSecurityQuestionsRequest buildValidRequest() {
        SetupSecurityQuestionsRequest req = new SetupSecurityQuestionsRequest();
        req.setUserId(1L);

        SetupSecurityQuestionsRequest.Item i1 = new SetupSecurityQuestionsRequest.Item();
        i1.setPreguntaId(1);
        i1.setRespuesta("uno");

        SetupSecurityQuestionsRequest.Item i2 = new SetupSecurityQuestionsRequest.Item();
        i2.setPreguntaId(2);
        i2.setRespuesta("dos");

        SetupSecurityQuestionsRequest.Item i3 = new SetupSecurityQuestionsRequest.Item();
        i3.setPreguntaId(3);
        i3.setRespuesta("tres");

        req.setItems(List.of(i1, i2, i3));
        return req;
    }

    private PreguntaSeguridad mockPregunta(int id) {
        PreguntaSeguridad p = new PreguntaSeguridad();
        p.setId(id);
        p.setTexto("P" + id);
        p.setActiva(true);
        return p;
    }
}
