package com.microservice.auth_service.microservice_auth_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.auth_service.microservice_auth_service.controller.UsuarioController;
import com.microservice.auth_service.microservice_auth_service.dto.UsuarioRequest;
import com.microservice.auth_service.microservice_auth_service.dto.UsuarioResponse;
import com.microservice.auth_service.microservice_auth_service.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll_debeRetornarListaUsuarios() throws Exception {
        UsuarioResponse resp = new UsuarioResponse(
                1L, "Juan", "juan@example.com", "ADMIN", "ACTIVO", 10, 100
        );

        when(usuarioService.getAll()).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/auth/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    @Test
    void create_debeRetornarUsuarioCreado() throws Exception {
        UsuarioRequest req = new UsuarioRequest();
        req.setNombre("Juan");
        req.setCorreo("juan@example.com");
        req.setClave("1234");
        req.setIdRol(1L);
        req.setIdEstado(1L);

        UsuarioResponse resp = new UsuarioResponse(
                1L, "Juan", "juan@example.com", "ADMIN", "ACTIVO", 0, 0
        );

        when(usuarioService.create(any(UsuarioRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/api/auth/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.correo").value("juan@example.com"));
    }

        @Test
    void getById_debeRetornarUsuario() throws Exception {
        UsuarioResponse resp = new UsuarioResponse(
                5L, "Juan", "juan@example.com", "ADMIN", "ACTIVO", 10, 100
        );

        when(usuarioService.getById(5L)).thenReturn(resp);

        mockMvc.perform(get("/api/auth/usuarios/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.correo").value("juan@example.com"));
    }

    @Test
    void update_debeRetornarUsuarioActualizado() throws Exception {
        UsuarioRequest req = new UsuarioRequest();
        req.setNombre("Juan Actualizado");
        req.setCorreo("juan2@example.com");
        req.setClave("abcd");
        req.setIdRol(2L);
        req.setIdEstado(1L);

        UsuarioResponse resp = new UsuarioResponse(
                5L, "Juan Actualizado", "juan2@example.com", "USER", "ACTIVO", 20, 200
        );

        when(usuarioService.update(eq(5L), any(UsuarioRequest.class))).thenReturn(resp);

        mockMvc.perform(put("/api/auth/usuarios/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.nombre").value("Juan Actualizado"))
                .andExpect(jsonPath("$.correo").value("juan2@example.com"));
    }

    @Test
    void delete_debeRetornarNoContent() throws Exception {
        // El controller hace try/catch, así que basta con llamar y verificar el 204
        mockMvc.perform(delete("/api/auth/usuarios/{id}", 5L))
                .andExpect(status().isNoContent());
    }

    @Test
    void getPuntajeGlobal_debeRetornarPuntaje() throws Exception {
        when(usuarioService.obtenerPuntajeGlobal(5L)).thenReturn(120);

        mockMvc.perform(get("/api/auth/usuarios/{id}/puntaje-global", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(120));
    }

    @Test
    void updatePuntajeGlobal_debeRetornarNuevoPuntaje() throws Exception {
        when(usuarioService.actualizarPuntajeGlobal(5L, 30)).thenReturn(150);

        mockMvc.perform(post("/api/auth/usuarios/{id}/puntaje-global", 5L)
                        .param("delta", "30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(150));
    }

}
