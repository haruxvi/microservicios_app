package com.microservice.game_service.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Service
public class AuthClient {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl = "http://auth-service";

    public void sumarPuntaje(Long usuarioId, int puntos) {
        try {
            String uri = String.format(baseUrl + "/api/auth/usuario/%s/puntaje-global", usuarioId);
            String body = objectMapper.writeValueAsString(Map.of("puntos", puntos));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .PUT(HttpRequest.BodyPublishers.ofString(body))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new RuntimeException("Error al sumar puntaje: HTTP " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error al llamar auth-service", e);
        }
    }

    public int obtenerPuntajeActual(Long usuarioId) {
        try {
            String uri = String.format(baseUrl + "/api/auth/usuario/%s/puntaje-global", usuarioId);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new RuntimeException("Error al obtener puntaje actual: HTTP " + response.statusCode());
            }

            String body = response.body().trim();
            // Si el body es solo un número
            if (body.matches("^-?\\d+$")) {
                return Integer.parseInt(body);
            }

            // Intentar parsear JSON como mapa y buscar campos comunes
            try {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = objectMapper.readValue(body, java.util.Map.class);
                for (String key : new String[]{"puntaje", "puntos", "score", "puntajeGlobal"}) {
                    if (map.containsKey(key) && map.get(key) instanceof Number) {
                        return ((Number) map.get(key)).intValue();
                    }
                }
            } catch (Exception ex) {
                // fallthrough
            }

            throw new RuntimeException("Respuesta inválida al obtener puntaje: " + body);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error al llamar auth-service", e);
        }
    }
}