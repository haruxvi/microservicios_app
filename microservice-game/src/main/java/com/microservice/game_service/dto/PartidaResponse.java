package com.microservice.game_service.dto;

import java.time.LocalDateTime;

public record PartidaResponse(
    Long id,
    Long usuarioId,
    String categoria,
    String dificultad,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    Integer puntajeFinal,
    String estado
) {}