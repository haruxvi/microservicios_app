package com.microservice.auth_service.microservice_auth_service.service;

import com.microservice.auth_service.microservice_auth_service.dto.SecurityQuestionDto;
import com.microservice.auth_service.microservice_auth_service.dto.SetupSecurityQuestionsRequest;
import com.microservice.auth_service.microservice_auth_service.model.PreguntaSeguridad;
import com.microservice.auth_service.microservice_auth_service.model.Usuario;
import com.microservice.auth_service.microservice_auth_service.repository.PreguntaSeguridadRepository;
import com.microservice.auth_service.microservice_auth_service.repository.RespuestaSeguridadRepository;
import com.microservice.auth_service.microservice_auth_service.repository.UsuarioRepository;
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
class SecurityQuestionsServiceUnitTest {

    @Mock PreguntaSeguridadRepository preguntaRepo;
    @Mock RespuestaSeguridadRepository respuestaRepo;
    @Mock UsuarioRepository usuarioRepo;
    @Mock BCryptPasswordEncoder passwordEncoder;

    @InjectMocks SecurityQuestionsService service;

    @Test
    void listarActivas_mapeaA_DTO() {
        PreguntaSeguridad p1 = new PreguntaSeguridad();
        p1.setId(1);
        p1.setTexto("¿Primera mascota?");
        PreguntaSeguridad p2 = new PreguntaSeguridad();
        p2.setId(2);
        p2.setTexto("¿Ciudad de nacimiento?");

        when(preguntaRepo.findByActivaTrue()).thenReturn(List.of(p1, p2));

        List<SecurityQuestionDto> out = service.listarActivas();

        assertEquals(2, out.size());
        assertEquals(1, out.get(0).id());
        assertEquals("¿Primera mascota?", out.get(0).texto());
    }

    @Test
    void setupPreguntas_userIdNull_lanzaIllegalArgument() {
        SetupSecurityQuestionsRequest req = new SetupSecurityQuestionsRequest();
        req.setUserId(null);
        req.setItems(List.of());

        var ex = assertThrows(IllegalArgumentException.class, () -> service.setupPreguntas(req));
        assertTrue(ex.getMessage().toLowerCase().contains("userid"));
        verifyNoInteractions(usuarioRepo, preguntaRepo, respuestaRepo, passwordEncoder);
    }

    @Test
    void setupPreguntas_itemsDistintoDe3_lanzaIllegalArgument() {
        SetupSecurityQuestionsRequest req = new SetupSecurityQuestionsRequest();
        req.setUserId(1L);
        req.setItems(List.of(item(1, "a"), item(2, "b"))); // 2 items

        var ex = assertThrows(IllegalArgumentException.class, () -> service.setupPreguntas(req));
        assertTrue(ex.getMessage().contains("exactamente 3"));
        verifyNoInteractions(usuarioRepo, preguntaRepo, respuestaRepo, passwordEncoder);
    }

    @Test
    void setupPreguntas_preguntasDuplicadas_lanzaIllegalArgument() {
        SetupSecurityQuestionsRequest req = new SetupSecurityQuestionsRequest();
        req.setUserId(1L);
        req.setItems(List.of(item(1, "a"), item(1, "b"), item(2, "c")));

        when(usuarioRepo.findById(1L)).thenReturn(Optional.of(new Usuario()));

        var ex = assertThrows(IllegalArgumentException.class, () -> service.setupPreguntas(req));
        assertTrue(ex.getMessage().toLowerCase().contains("repetir"));

        verify(usuarioRepo).findById(1L);
        verifyNoMoreInteractions(usuarioRepo);
        verifyNoInteractions(preguntaRepo, respuestaRepo, passwordEncoder);
    }

    @Test
    void setupPreguntas_preguntaInvalida_lanzaRuntimeException() {
        SetupSecurityQuestionsRequest req = new SetupSecurityQuestionsRequest();
        req.setUserId(1L);
        req.setItems(List.of(item(1, "a"), item(2, "b"), item(3, "c")));

        when(usuarioRepo.findById(1L)).thenReturn(Optional.of(new Usuario()));

        // OJO: en el service se usan Set/HashSet, por lo que el orden de validación de IDs no es determinístico.
        // Para evitar UnnecessaryStubbingException en Mockito STRICT, usamos un Answer único.
        when(preguntaRepo.findById(anyInt())).thenAnswer(inv -> {
            Integer pid = inv.getArgument(0);
            return pid == 2 ? Optional.empty() : Optional.of(new PreguntaSeguridad());
        });

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.setupPreguntas(req));
        assertTrue(ex.getMessage().contains("Pregunta inválida"));

        verify(respuestaRepo, never()).save(any());
    }

    @Test
    void setupPreguntas_siYaTeniaRespuestas_eliminaYRecrea() {
        SetupSecurityQuestionsRequest req = new SetupSecurityQuestionsRequest();
        req.setUserId(1L);
        req.setItems(List.of(item(1, "uno"), item(2, "dos"), item(3, "tres")));

        when(usuarioRepo.findById(1L)).thenReturn(Optional.of(new Usuario()));
        when(preguntaRepo.findById(anyInt())).thenReturn(Optional.of(new PreguntaSeguridad()));
        when(respuestaRepo.existsByUsuarioId(1L)).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenAnswer(inv -> "hash:" + inv.getArgument(0));

        service.setupPreguntas(req);

        verify(respuestaRepo).deleteByUsuarioId(1L);
        verify(respuestaRepo, times(3)).save(any());

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(passwordEncoder, atLeastOnce()).encode(captor.capture());
        assertTrue(captor.getAllValues().stream().anyMatch(v -> v.equals("uno")));
    }

    @Test
    void setupPreguntas_respuestaVacia_lanzaIllegalArgument() {
        SetupSecurityQuestionsRequest req = new SetupSecurityQuestionsRequest();
        req.setUserId(1L);
        req.setItems(List.of(item(1, "uno"), item(2, " "), item(3, "tres")));

        when(usuarioRepo.findById(1L)).thenReturn(Optional.of(new Usuario()));
        when(preguntaRepo.findById(anyInt())).thenReturn(Optional.of(new PreguntaSeguridad()));

        var ex = assertThrows(IllegalArgumentException.class,
                () -> service.setupPreguntas(req));

        assertTrue(ex.getMessage().toLowerCase().contains("respuesta"));
    }



    private static SetupSecurityQuestionsRequest.Item item(int preguntaId, String respuesta) {
        SetupSecurityQuestionsRequest.Item i = new SetupSecurityQuestionsRequest.Item();
        i.setPreguntaId(preguntaId);
        i.setRespuesta(respuesta);
        return i;
    }
}
