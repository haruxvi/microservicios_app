package com.microservice.feedbak_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.feedbak_service.controller.FeedbackController;
import com.microservice.feedbak_service.dto.FeedbackRequest;
import com.microservice.feedbak_service.dto.FeedbackResponse;
import com.microservice.feedbak_service.service.FeedbackService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")                 // 👈 usa el profile de test (application.properties de test)
@WebMvcTest(controllers = FeedbackController.class)
class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedbackService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crear_debeRetornarFeedbackResponse() throws Exception {
        FeedbackRequest req = new FeedbackRequest(
                10L,
                "Mensaje desde el controller test",
                "error",
                "quiz"
        );

        FeedbackResponse resp = new FeedbackResponse(
                1L,
                "Mensaje desde el controller test",
                "ERROR",
                "QUIZ",
                LocalDateTime.now(),
                false,
                10L
        );

        when(service.crear(any(FeedbackRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/api/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.mensaje").value("Mensaje desde el controller test"))
                .andExpect(jsonPath("$.tipo").value("ERROR"))
                .andExpect(jsonPath("$.destino").value("QUIZ"))
                .andExpect(jsonPath("$.usuarioId").value(10L));
    }

    @Test
    void get_todos_debeRetornarLista() throws Exception {
        FeedbackResponse resp = new FeedbackResponse(
                1L,
                "Mensaje lista",
                "INFO",
                "ADMIN",
                LocalDateTime.now(),
                false,
                10L
        );

        when(service.todos()).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/feedback"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mensaje").value("Mensaje lista"));
    }

    @Test
    void get_pendientes_debeRetornarLista() throws Exception {
        FeedbackResponse resp = new FeedbackResponse(
                1L,
                "Pendiente",
                "ERROR",
                "ADMIN",
                LocalDateTime.now(),
                false,
                10L
        );

        when(service.pendientes()).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/feedback/pendientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].resuelto").value(false));
    }

    @Test
    void get_porUsuario_debeRetornarSoloDelUsuario() throws Exception {
        FeedbackResponse resp = new FeedbackResponse(
                1L,
                "Mensaje user",
                "ERROR",
                "QUIZ",
                LocalDateTime.now(),
                false,
                10L
        );

        when(service.porUsuario(10L)).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/feedback/usuario/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioId").value(10L));
    }

    @Test
    void patch_resolver_debeRetornarFeedbackResuelto() throws Exception {
        FeedbackResponse resp = new FeedbackResponse(
                1L,
                "Mensaje resuelto",
                "ERROR",
                "ADMIN",
                LocalDateTime.now(),
                true,
                10L
        );

        when(service.resolver(anyLong())).thenReturn(resp);

        mockMvc.perform(patch("/api/feedback/1/resolver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resuelto").value(true));
    }
}
