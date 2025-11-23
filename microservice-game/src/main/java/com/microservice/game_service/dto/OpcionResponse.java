package com.microservice.game_service.dto;

public record OpcionResponse(
        Long id,
        String texto,
        boolean correcta
) {}
