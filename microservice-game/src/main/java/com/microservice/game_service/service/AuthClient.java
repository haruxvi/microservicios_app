package com.microservice.game_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AuthClient {

    private final WebClient webClient;

    public AuthClient(
            WebClient.Builder builder,
            @Value("${auth.service.base-url:http://localhost:8081}") String baseUrl
    ) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    /**
     * Obtiene el puntaje global actual del usuario desde auth-service.
     * Supone un endpoint: GET /api/auth/usuarios/{id}/puntaje-global
     */
    public int obtenerPuntajeGlobal(Long usuarioId) {
        Integer puntaje = webClient
                .get()
                .uri("/api/auth/usuarios/{id}/puntaje-global", usuarioId)
                .retrieve()
                .bodyToMono(Integer.class)
                .block();

        return puntaje != null ? puntaje : 0;
    }

    /**
     * Actualiza el puntaje global sumando delta.
     * Supone un endpoint: POST /api/auth/usuarios/{id}/puntaje-global?delta={delta}
     * que devuelve el nuevo puntaje global.
     */
    public int actualizarPuntajeGlobal(Long usuarioId, int delta) {
        Integer nuevoPuntaje = webClient
                .post()
                .uri("/api/auth/usuarios/{id}/puntaje-global?delta={delta}", usuarioId, delta)
                .retrieve()
                .bodyToMono(Integer.class)
                .block();

        return nuevoPuntaje != null ? nuevoPuntaje : 0;
    }
}
