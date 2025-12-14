package com.microservice.auth_service.microservice_auth_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.auth_service.microservice_auth_service.dto.*;
import com.microservice.auth_service.microservice_auth_service.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UsuarioControllerUnitTest {

    private MockMvc mvc;
    private UsuarioService usuarioService;
    private ObjectMapper om;

    @BeforeEach
    void setup() {
        usuarioService = Mockito.mock(UsuarioService.class);
        om = new ObjectMapper();

        UsuarioController controller = new UsuarioController(usuarioService);

        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAll_ok() throws Exception {
        UsuarioResponse resp = new UsuarioResponse(
                1L, "Juan", "juan@example.com", "ADMIN", "ACTIVO", 10, 100, null
        );
        when(usuarioService.getAll()).thenReturn(List.of(resp));

        mvc.perform(get("/api/auth/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    @Test
    void create_ok() throws Exception {
        UsuarioRequest req = new UsuarioRequest();
        req.setNombre("Juan");
        req.setCorreo("juan@example.com");
        req.setClave("1234");
        req.setIdRol(1L);
        req.setIdEstado(1L);

        UsuarioResponse resp = new UsuarioResponse(
                1L, "Juan", "juan@example.com", "ADMIN", "ACTIVO", 0, 0, null
        );
        when(usuarioService.create(any(UsuarioRequest.class))).thenReturn(resp);

        mvc.perform(post("/api/auth/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk()) // si tu controller devuelve 201, cambia a isCreated()
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.correo").value("juan@example.com"));
    }

    @Test
    void getById_ok() throws Exception {
        UsuarioResponse resp = new UsuarioResponse(
                5L, "Juan", "juan@example.com", "ADMIN", "ACTIVO", 10, 100, null
        );
        when(usuarioService.getById(5L)).thenReturn(resp);

        mvc.perform(get("/api/auth/usuarios/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.correo").value("juan@example.com"));
    }

    @Test
    void update_ok() throws Exception {
        UsuarioRequest req = new UsuarioRequest();
        req.setNombre("Juan Actualizado");
        req.setCorreo("juan2@example.com");
        req.setClave("abcd");
        req.setIdRol(2L);
        req.setIdEstado(1L);

        UsuarioResponse resp = new UsuarioResponse(
                5L, "Juan Actualizado", "juan2@example.com", "USER", "ACTIVO", 20, 200, null
        );
        when(usuarioService.update(eq(5L), any(UsuarioRequest.class))).thenReturn(resp);

        mvc.perform(put("/api/auth/usuarios/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("juan2@example.com"));
    }

    @Test
    void delete_ok_204() throws Exception {
        // doNothing() es default en Mockito para void, esto es opcional
        mvc.perform(delete("/api/auth/usuarios/{id}", 5L))
                .andExpect(status().isNoContent());
    }

    @Test
    void getPuntajeGlobal_ok() throws Exception {
        when(usuarioService.obtenerPuntajeGlobal(5L)).thenReturn(120);

        mvc.perform(get("/api/auth/usuarios/{id}/puntaje-global", 5L))
                .andExpect(status().isOk())
                .andExpect(content().string("120"));
    }

    @Test
    void updatePuntajeGlobal_ok() throws Exception {
        when(usuarioService.actualizarPuntajeGlobal(5L, 30)).thenReturn(150);

        mvc.perform(post("/api/auth/usuarios/{id}/puntaje-global", 5L)
                        .param("delta", "30"))
                .andExpect(status().isOk())
                .andExpect(content().string("150"));
    }

    @Test
    void delete_conflict_siFK() throws Exception {
        doThrow(new DataIntegrityViolationException("FK")).when(usuarioService).delete(5L);

        mvc.perform(delete("/api/auth/usuarios/{id}", 5L))
                .andExpect(status().isConflict());
        // Si tu handler retorna mensaje, puedes volver a agregar:
        // .andExpect(content().string(containsString("No se puede eliminar")));
    }

    @Test
    void actualizarFotoPerfil_ok_204() throws Exception {
        ActualizarFotoPerfilRequest req = new ActualizarFotoPerfilRequest("aW1n");

        mvc.perform(patch("/api/auth/usuarios/{id}/foto-perfil", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isNoContent());
    }

    @Test
    void login_ok() throws Exception {
        LoginRequest req = new LoginRequest("juan@example.com", "1234");
        UsuarioResponse resp = new UsuarioResponse(
                5L, "Juan", "juan@example.com", "ADMIN", "ACTIVO", 10, 100, null
        );

        when(usuarioService.loginPorIdentificadorYClave(eq("juan@example.com"), eq("1234")))
                .thenReturn(resp);

        mvc.perform(post("/api/auth/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L));
    }

    @Test
    void login_credencialesInvalidas_401() throws Exception {
        LoginRequest req = new LoginRequest("juan@example.com", "mal");

        when(usuarioService.loginPorIdentificadorYClave(any(), any()))
                .thenThrow(new RuntimeException("Credenciales inválidas"));

        mvc.perform(post("/api/auth/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void actualizarPasswordPorCorreo_204() throws Exception {
        mvc.perform(put("/api/auth/usuarios/password")
                        .param("identificador", "juan@example.com")
                        .param("nuevaClave", "nueva"))
                .andExpect(status().isNoContent());
    }
}
