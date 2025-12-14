package com.microservice.game_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Petición enviada para finalizar una partida y registrar el puntaje obtenido")
public record FinalizarPartidaRequest(

        @Schema(
                description = "ID de la partida a finalizar",
                example = "120"
        )
        @NotNull(message = "El ID de partida es obligatorio")
        Long partidaId,

        @Schema(
                description = "Puntaje obtenido por el usuario durante la partida",
                example = "8"
        )
        @Min(value = 0, message = "El puntaje no puede ser negativo")
        int puntajeObtenido

) {}

