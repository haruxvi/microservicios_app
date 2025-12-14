package com.microservice.auth_service.microservice_auth_service.service;

import com.microservice.auth_service.microservice_auth_service.controller.ResourceNotFoundException;
import com.microservice.auth_service.microservice_auth_service.dto.RecoveryQuestionsResponse;
import com.microservice.auth_service.microservice_auth_service.dto.ResetPasswordRequest;
import com.microservice.auth_service.microservice_auth_service.dto.VerifyRecoveryRequest;
import com.microservice.auth_service.microservice_auth_service.model.PreguntaSeguridad;
import com.microservice.auth_service.microservice_auth_service.model.RespuestaSeguridad;
import com.microservice.auth_service.microservice_auth_service.model.Usuario;
import com.microservice.auth_service.microservice_auth_service.repository.PreguntaSeguridadRepository;
import com.microservice.auth_service.microservice_auth_service.repository.RespuestaSeguridadRepository;
import com.microservice.auth_service.microservice_auth_service.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordRecoveryServiceUnitTest {

    @Mock UsuarioRepository usuarioRepo;
    @Mock PreguntaSeguridadRepository preguntaRepo;
    @Mock RespuestaSeguridadRepository respuestaRepo;
    @Mock PasswordEncoder passwordEncoder;
    @Mock PasswordRecoveryTokenService tokenService;

    @InjectMocks PasswordRecoveryService service;

    @Test
    void getQuestions_identificadorVacio_lanzaIllegalArgument() {
        var ex = assertThrows(IllegalArgumentException.class, () -> service.getQuestions(" "));
        assertTrue(ex.getMessage().toLowerCase().contains("identificador"));
    }

    @Test
    void getQuestions_usuarioNoExiste_lanzaNotFound() {
        when(usuarioRepo.findByCorreoIgnoreCase("x@x.com")).thenReturn(Optional.empty());
        when(usuarioRepo.findByNombreIgnoreCase("x@x.com")).thenReturn(Optional.empty());

        var ex = assertThrows(ResourceNotFoundException.class, () -> service.getQuestions("x@x.com"));
        assertTrue(ex.getMessage().toLowerCase().contains("usuario"));
    }

    @Test
    void getQuestions_sin3Preguntas_lanzaNotFound() {
        Usuario u = new Usuario();
        u.setId(1L);

        when(usuarioRepo.findByCorreoIgnoreCase("a@a.com")).thenReturn(Optional.of(u));
        when(respuestaRepo.findByUsuarioId(1L)).thenReturn(List.of(new RespuestaSeguridad())); // 1 sola

        var ex = assertThrows(ResourceNotFoundException.class, () -> service.getQuestions("a@a.com"));
        assertTrue(ex.getMessage().contains("3 preguntas"));
    }

    @Test
    void getQuestions_ok_devuelvePreguntas() {
        Usuario u = new Usuario();
        u.setId(1L);

        when(usuarioRepo.findByCorreoIgnoreCase("a@a.com")).thenReturn(Optional.of(u));

        RespuestaSeguridad r1 = resp(1L, 10, "hash10");
        RespuestaSeguridad r2 = resp(1L, 20, "hash20");
        RespuestaSeguridad r3 = resp(1L, 30, "hash30");
        when(respuestaRepo.findByUsuarioId(1L)).thenReturn(List.of(r1, r2, r3));

        when(preguntaRepo.findById(10)).thenReturn(Optional.of(preg(10, "P10")));
        when(preguntaRepo.findById(20)).thenReturn(Optional.of(preg(20, "P20")));
        when(preguntaRepo.findById(30)).thenReturn(Optional.of(preg(30, "P30")));

        RecoveryQuestionsResponse out = service.getQuestions("a@a.com");

        assertEquals(1L, out.userId());
        assertEquals(3, out.questions().size());
        assertEquals(10, out.questions().get(0).id());
        assertEquals("P10", out.questions().get(0).texto());
    }

    @Test
    void verifyAnswers_payloadInvalido_lanzaIllegalArgument() {
        var ex = assertThrows(IllegalArgumentException.class,
                () -> service.verifyAnswers(new VerifyRecoveryRequest(null, List.of())));
        assertTrue(ex.getMessage().toLowerCase().contains("userid"));
    }

    @Test
    void verifyAnswers_usuarioSin3Preguntas_lanzaNotFound() {
        when(usuarioRepo.findById(1L)).thenReturn(Optional.of(new Usuario()));
        when(respuestaRepo.findByUsuarioId(1L)).thenReturn(List.of());

        var ex = assertThrows(ResourceNotFoundException.class,
                () -> service.verifyAnswers(new VerifyRecoveryRequest(1L, List.of(item(10, "a"), item(20, "b"), item(30, "c")))));
        assertTrue(ex.getMessage().contains("3 preguntas"));
    }

    @Test
    void verifyAnswers_respuestaIncorrecta_lanzaIllegalArgument() {
        when(usuarioRepo.findById(1L)).thenReturn(Optional.of(new Usuario()));
        when(respuestaRepo.findByUsuarioId(1L)).thenReturn(List.of(
                resp(1L, 10, "hash10"),
                resp(1L, 20, "hash20"),
                resp(1L, 30, "hash30")
        ));

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false); // falla
        var ex = assertThrows(IllegalArgumentException.class,
                () -> service.verifyAnswers(new VerifyRecoveryRequest(1L, List.of(item(10, "a"), item(20, "b"), item(30, "c")))));
        assertTrue(ex.getMessage().toLowerCase().contains("incorrectas"));
        verify(tokenService, never()).create(anyLong());
    }

    @Test
    void verifyAnswers_ok_devuelveToken() {
        when(usuarioRepo.findById(1L)).thenReturn(Optional.of(new Usuario()));
        when(respuestaRepo.findByUsuarioId(1L)).thenReturn(List.of(
                resp(1L, 10, "hash10"),
                resp(1L, 20, "hash20"),
                resp(1L, 30, "hash30")
        ));

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(tokenService.create(1L)).thenReturn("t0k3n");

        String token = service.verifyAnswers(new VerifyRecoveryRequest(1L, List.of(item(10, "a"), item(20, "b"), item(30, "c"))));

        assertEquals("t0k3n", token);
        verify(tokenService).create(1L);
    }

    @Test
    void resetPassword_passwordCorta_lanzaIllegalArgument() {
        var ex = assertThrows(IllegalArgumentException.class,
                () -> service.resetPassword(new ResetPasswordRequest("token", "123")));
        assertTrue(ex.getMessage().toLowerCase().contains("8"));
    }

    @Test
    void resetPassword_ok_cambiaClave() {
        when(tokenService.consume("token")).thenReturn(1L);

        Usuario u = new Usuario();
        u.setId(1L);
        when(usuarioRepo.findById(1L)).thenReturn(Optional.of(u));

        when(passwordEncoder.encode("NuevaClave#2025")).thenReturn("HASHED");
        service.resetPassword(new ResetPasswordRequest("token", "NuevaClave#2025"));

        assertEquals("HASHED", u.getClave());
        verify(usuarioRepo).save(u);
    }

    private static RespuestaSeguridad resp(Long userId, int pid, String hash) {
        RespuestaSeguridad r = new RespuestaSeguridad();
        r.setUsuarioId(userId);
        r.setPreguntaId(pid);
        r.setRespuestaHash(hash);
        return r;
    }

    private static PreguntaSeguridad preg(int id, String texto) {
        PreguntaSeguridad p = new PreguntaSeguridad();
        p.setId(id);
        p.setTexto(texto);
        p.setActiva(true);
        return p;
    }

    private static VerifyRecoveryRequest.Item item(int pid, String resp) {
        return new VerifyRecoveryRequest.Item(pid, resp);
    }
}
