package com.microservice.game_service.dto;

import java.util.List;

public record PreguntaResponse(
    Long id,
    String enunciado,
    Integer puntaje,
    List<OpcionResponse> opciones
) {}