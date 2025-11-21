package com.microservice.game_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

// QuizClient.java
@Service
public class QuizClient {
    private final WebClient webClient = WebClient.create();

    public List<PreguntaResponse> obtenerPreguntas(Long categoriaId, Long dificultadId) {
        return webClient.get()
            .uri("http://quiz-service/api/quiz/preguntas?categoriaId={cat}&dificultadId={dif}", categoriaId, dificultadId)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<PreguntaResponse>>() {})
            .block();
    }
}

// AuthClient.java
