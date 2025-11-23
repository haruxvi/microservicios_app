package com.microservice.game_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record FinalizarPartidaRequest(
        @NotNull(message = "El ID de partida es obligatorio")
        Long partidaId,

        @Min(value = 0, message = "El puntaje no puede ser negativo")
        int puntajeObtenido
) {}
