package com.microservice.game_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.game_service.dto.*;
import com.microservice.game_service.service.PartidaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.microservice.game_service.controller.PartidaController;


@WebMvcTest(controllers = PartidaController.class)
@AutoConfigureMockMvc
@WithMockUser   // <- Simula un usuario autenticado en TODOS los tests
class PartidaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartidaService partidaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void iniciarPartida_debeRetornar200YBodyCorrecto() throws Exception {
        // Arrange
        IniciarPartidaRequest request = new IniciarPartidaRequest(
                1L,   // usuarioId
                2L,   // categoriaId
                3L    // dificultadId
        );

        LocalDateTime ahora = LocalDateTime.now();
        IniciarPartidaResponse respuestaServicio = new IniciarPartidaResponse(
                10L,
                ahora,
                List.of()  // lista de preguntas vacía para simplificar
        );

        when(partidaService.iniciarPartida(any(IniciarPartidaRequest.class)))
                .thenReturn(respuestaServicio);

        // Act & Assert
        mockMvc.perform(
                        post("/api/game/partidas/iniciar")
                                .with(csrf()) // <- IMPORTANTE: token CSRF para evitar 403
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partidaId").value(10L))
                .andExpect(jsonPath("$.fechaInicio").exists());
    }

    @Test
    void finalizarPartida_debeRetornar200YBodyCorrecto() throws Exception {
        // Arrange
        FinalizarPartidaRequest request = new FinalizarPartidaRequest(
                10L, // partidaId
                80   // puntajeObtenido
        );

        LocalDateTime fin = LocalDateTime.now();
        FinalizarPartidaResponse respuestaServicio = new FinalizarPartidaResponse(
                10L,
                80,
                100,   // puntajeAnterior
                180,   // nuevoPuntajeGlobal
                fin,
                "Partida finalizada exitosamente"
        );

        when(partidaService.finalizarPartida(any(FinalizarPartidaRequest.class)))
                .thenReturn(respuestaServicio);

        // Act & Assert
        mockMvc.perform(
                        post("/api/game/partidas/finalizar")
                                .with(csrf()) // <- también necesita CSRF
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partidaId").value(10L))
                .andExpect(jsonPath("$.puntajeObtenido").value(80))
                .andExpect(jsonPath("$.puntajeNuevoGlobal").value(180))
                .andExpect(jsonPath("$.mensaje").value("Partida finalizada exitosamente"));
    }

    @Test
    void obtenerPartidasPorUsuario_debeRetornarListaDePartidas() throws Exception {
        // Arrange
        Long usuarioId = 1L;

        LocalDateTime inicio = LocalDateTime.now().minusMinutes(10);
        LocalDateTime fin = LocalDateTime.now();

        PartidaResponse partida1 = new PartidaResponse(
                10L,
                usuarioId,
                "Categoría #2",
                "Dificultad #3",
                inicio,
                fin,
                100,
                "Estado #2"
        );

        PartidaResponse partida2 = new PartidaResponse(
                11L,
                usuarioId,
                "Categoría #3",
                "Dificultad #1",
                inicio,
                null,
                50,
                "Estado #1"
        );

        when(partidaService.obtenerPartidasPorUsuario(eq(usuarioId)))
                .thenReturn(List.of(partida1, partida2));

        // Act & Assert
        mockMvc.perform(
                        get("/api/game/partidas/usuario/{usuarioId}", usuarioId)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(10L))
                .andExpect(jsonPath("$[0].usuarioId").value(usuarioId))
                .andExpect(jsonPath("$[0].puntajeFinal").value(100))
                .andExpect(jsonPath("$[1].id").value(11L));
    }
}
