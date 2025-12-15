package com.microservice.game_service.service;

import com.microservice.game_service.dto.PreguntaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class QuizClient {

    private final WebClient webClient;

    public QuizClient(
            WebClient.Builder builder,
            @Value("${quiz.service.base-url:http://localhost:8082}") String baseUrl
    ) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    /**
     * Obtiene preguntas desde quiz-service filtradas por categoría y dificultad.
     * Asume endpoint: GET /api/quiz/preguntas/categoria/{cat}/dificultad/{dif}
     */
    public List<PreguntaResponse> obtenerPreguntas(Long categoriaId, Long dificultadId) {
        return webClient
                .get()
                .uri("/api/quiz/preguntas/categoria/{cat}/dificultad/{dif}",
                        categoriaId, dificultadId)
                .retrieve()
                .bodyToFlux(PreguntaResponse.class)
                .collectList()
                .block();
    }
}
