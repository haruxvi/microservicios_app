package com.microservice.quiz_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.quiz_service.controller.PreguntaController;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
    void deletePregunta_debeRetornarNoContent() throws Exception {
        Long id = 5L;
        doNothing().when(preguntaService).delete(id);

        mockMvc.perform(delete("/api/quiz/preguntas/{id}", id))
                .andExpect(status().isNoContent());

        verify(preguntaService).delete(id);
    }
}
