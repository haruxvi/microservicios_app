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
}
