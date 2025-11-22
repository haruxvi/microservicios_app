package com.microservice.game_service.service;









import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.game_service.dto.PreguntaResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class QuizClient {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<PreguntaResponse> obtenerPreguntas(Long categoriaId, Long dificultadId) {
        try {
            String uri = String.format("http://quiz-service/api/quiz/preguntas?categoriaId=%s&dificultadId=%s",
                    categoriaId, dificultadId);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return objectMapper.readValue(response.body(), new TypeReference<List<PreguntaResponse>>() {});
            }
            throw new RuntimeException("Error al obtener preguntas: HTTP " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error al llamar quiz-service", e);
        }
    }
}