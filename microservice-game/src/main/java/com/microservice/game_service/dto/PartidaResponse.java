package com.microservice.game_service.dto;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa una partida registrada en el sistema, incluyendo información del usuario, categoría, puntaje y estado final")
public record PartidaResponse(

        @Schema(
                description = "ID único de la partida",
                example = "120"
        )
        Long id,

        @Schema(
                description = "ID del usuario que jugó la partida",
                example = "5"
        )
        Long usuarioId,

        @Schema(
                description = "Nombre de la categoría jugada",
                example = "Ciencia"
        )
        String categoria,

        @Schema(
                description = "Nombre de la dificultad de la partida",
                example = "Intermedio"
        )
        String dificultad,

        @Schema(
                description = "Fecha y hora en que comenzó la partida",
                example = "2025-12-01T14:30:00"
        )
        LocalDateTime fechaInicio,

        @Schema(
                description = "Fecha y hora en que terminó la partida",
                example = "2025-12-01T14:45:40"
        )
        LocalDateTime fechaFin,

        @Schema(
                description = "Puntaje final obtenido por el usuario al finalizar la partida",
                example = "8"
        )
        Integer puntajeFinal,

        @Schema(
                description = "Estado de la partida (ej. COMPLETADA, CANCELADA, PENDIENTE)",
                example = "COMPLETADA"
        )
        String estado

) {}

