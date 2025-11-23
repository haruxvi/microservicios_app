package com.microservice.game_service;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.game_service.controller.PartidaController;
import com.microservice.game_service.dto.FinalizarPartidaRequest;
import com.microservice.game_service.dto.FinalizarPartidaResponse;
import com.microservice.game_service.dto.IniciarPartidaRequest;
import com.microservice.game_service.dto.IniciarPartidaResponse;
import com.microservice.game_service.service.PartidaService;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class GameControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private PartidaController controller;

    @Mock
    private PartidaService service;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void iniciarPartida() throws Exception {
        var req = new IniciarPartidaRequest(1L, 1L, 1L);
        var res = new IniciarPartidaResponse(1L, LocalDateTime.now(), List.of());
        when(service.iniciarPartida(any())).thenReturn(res);

        mockMvc.perform(post("/api/game/iniciar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partidaId").value(1));
    }

    @Test
    void finalizarPartida() throws Exception {
        var req = new FinalizarPartidaRequest(1L, 900);
        var res = new FinalizarPartidaResponse(1L, 900, 2000, 2900, LocalDateTime.now(), "Partida finalizada exitosamente");
        when(service.finalizarPartida(any())).thenReturn(res);

        mockMvc.perform(post("/api/game/finalizar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.puntajeNuevoTotal").value(2900))
                .andExpect(jsonPath("$.mensaje").value("Partida finalizada exitosamente"));
    }
}