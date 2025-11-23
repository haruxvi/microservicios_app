package com.microservice.auth_service.microservice_auth_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.auth_service.microservice_auth_service.controller.EstadoController;
import com.microservice.auth_service.microservice_auth_service.dto.EstadoRequest;
import com.microservice.auth_service.microservice_auth_service.dto.EstadoResponse;
import com.microservice.auth_service.microservice_auth_service.service.EstadoService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(controllers = EstadoController.class)
@AutoConfigureMockMvc(addFilters = false)
class EstadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstadoService estadoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll_debeRetornarListaDeEstados() throws Exception {
        EstadoResponse resp = new EstadoResponse(1L, "ACTIVO");
        when(estadoService.getAll()).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/auth/estados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("ACTIVO"));
    }

    @Test
    void create_debeRetornarEstadoCreado() throws Exception {
        EstadoRequest request = new EstadoRequest("SUSPENDIDO");
        EstadoResponse resp = new EstadoResponse(2L, "SUSPENDIDO");

        when(estadoService.create(any(EstadoRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/api/auth/estados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.nombre").value("SUSPENDIDO"));
    }
}
