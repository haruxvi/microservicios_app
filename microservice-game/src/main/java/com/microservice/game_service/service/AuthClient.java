package com.microservice.game_service.service;

import org.springframework.stereotype.Service;

@Service
public class AuthClient {
    private final WebClient webClient = WebClient.create();

    public void sumarPuntaje(Long usuarioId, int puntos) {
        webClient.put()
            .uri("http://auth-service/api/auth/usuario/{id}/puntaje-global", usuarioId)
            .bodyValue(Map.of("puntos", puntos))
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
}