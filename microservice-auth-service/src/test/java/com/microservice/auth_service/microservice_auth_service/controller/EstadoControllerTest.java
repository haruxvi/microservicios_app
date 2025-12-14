package com.microservice.auth_service.microservice_auth_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.auth_service.microservice_auth_service.dto.EstadoRequest;
import com.microservice.auth_service.microservice_auth_service.dto.EstadoResponse;
import com.microservice.auth_service.microservice_auth_service.service.EstadoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EstadoControllerTest {

    private MockMvc mockMvc;
    private EstadoService estadoService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        estadoService = Mockito.mock(EstadoService.class);
        objectMapper = new ObjectMapper();

        EstadoController controller = new EstadoController(estadoService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("SUSPENDIDO"));
    }
}
