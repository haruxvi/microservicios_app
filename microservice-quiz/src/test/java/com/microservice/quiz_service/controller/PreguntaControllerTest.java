package com.microservice.quiz_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.quiz_service.dto.OpcionRequest;
import com.microservice.quiz_service.dto.OpcionResponse;
import com.microservice.quiz_service.dto.PreguntaRequest;
import com.microservice.quiz_service.dto.PreguntaResponse;
import com.microservice.quiz_service.services.PreguntaService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PreguntaController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class PreguntaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PreguntaService preguntaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPregunta_debeRetornarOkConRespuesta() throws Exception {
        PreguntaRequest req = new PreguntaRequest();
        req.setEnunciado("¿2+2?");
        req.setIdCategoria(1L);
        req.setIdDificultad(1L);
        req.setIdEstado(1L);
        req.setOpciones(List.of(new OpcionRequest("4", true)));

        PreguntaResponse resp = new PreguntaResponse(
                10L,
                "¿2+2?",
                "Matemáticas",
                "FÁCIL",
                "ACTIVA",
                List.of(new OpcionResponse(1L, "4", true))
        );

        when(preguntaService.create(any(PreguntaRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/api/quiz/preguntas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.enunciado").value("¿2+2?"));
    }

        @Test
    void getAll_debeRetornarListaPreguntas() throws Exception {
        PreguntaResponse resp = new PreguntaResponse(
                10L,
                "¿2+2?",
                "Matemáticas",
                "FÁCIL",
                "ACTIVA",
                List.of(new OpcionResponse(1L, "4", true))
        );

        when(preguntaService.getAll()).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/quiz/preguntas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L))
                .andExpect(jsonPath("$[0].enunciado").value("¿2+2?"));

        verify(preguntaService).getAll();
    }

    @Test
    void getById_debeRetornarPregunta() throws Exception {
        PreguntaResponse resp = new PreguntaResponse(
                10L,
                "¿2+2?",
                "Matemáticas",
                "FÁCIL",
                "ACTIVA",
                List.of(new OpcionResponse(1L, "4", true))
        );

        when(preguntaService.getById(10L)).thenReturn(resp);

        mockMvc.perform(get("/api/quiz/preguntas/{id}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.enunciado").value("¿2+2?"));

        verify(preguntaService).getById(10L);
    }

    @Test
    void getByCategoria_debeRetornarPreguntasFiltradas() throws Exception {
        PreguntaResponse resp = new PreguntaResponse(
                10L,
                "¿2+2?",
                "Matemáticas",
                "FÁCIL",
                "ACTIVA",
                List.of(new OpcionResponse(1L, "4", true))
        );

        when(preguntaService.getByCategoria(1L)).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/quiz/preguntas/categoria/{idCategoria}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L));

        verify(preguntaService).getByCategoria(1L);
    }

    @Test
    void getByDificultad_debeRetornarPreguntasFiltradas() throws Exception {
        PreguntaResponse resp = new PreguntaResponse(
                10L,
                "¿2+2?",
                "Matemáticas",
                "FÁCIL",
                "ACTIVA",
                List.of(new OpcionResponse(1L, "4", true))
        );

        when(preguntaService.getByDificultad(1L)).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/quiz/preguntas/dificultad/{idDificultad}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L));

        verify(preguntaService).getByDificultad(1L);
    }

    @Test
    void getByCategoriaAndDificultad_debeRetornarPreguntasFiltradas() throws Exception {
        PreguntaResponse resp = new PreguntaResponse(
                10L,
                "¿2+2?",
                "Matemáticas",
                "FÁCIL",
                "ACTIVA",
                List.of(new OpcionResponse(1L, "4", true))
        );

        when(preguntaService.getByCategoriaAndDificultad(1L, 1L))
                .thenReturn(List.of(resp));

        mockMvc.perform(get("/api/quiz/preguntas/categoria/{idCategoria}/dificultad/{idDificultad}", 1L, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L));

        verify(preguntaService).getByCategoriaAndDificultad(1L, 1L);
    }

    @Test
    void updatePregunta_debeRetornarOkConRespuesta() throws Exception {
        PreguntaRequest req = new PreguntaRequest();
        req.setEnunciado("¿3+3?");
        req.setIdCategoria(1L);
        req.setIdDificultad(1L);
        req.setIdEstado(1L);
        req.setOpciones(List.of(new OpcionRequest("6", true)));

        PreguntaResponse resp = new PreguntaResponse(
                10L,
                "¿3+3?",
                "Matemáticas",
                "FÁCIL",
                "ACTIVA",
                List.of(new OpcionResponse(1L, "6", true))
        );

        when(preguntaService.update(eq(10L), any(PreguntaRequest.class)))
                .thenReturn(resp);

        mockMvc.perform(put("/api/quiz/preguntas/{id}", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.enunciado").value("¿3+3?"));

        verify(preguntaService).update(eq(10L), any(PreguntaRequest.class));
    }


    @Test
    void deletePregunta_debeRetornarNoContent() throws Exception {
        Long id = 5L;
        doNothing().when(preguntaService).delete(id);

        mockMvc.perform(delete("/api/quiz/preguntas/{id}", id))
                .andExpect(status().isNoContent());

        verify(preguntaService).delete(id);
    }
}
