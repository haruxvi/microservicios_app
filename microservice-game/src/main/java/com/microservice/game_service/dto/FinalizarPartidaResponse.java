package com.microservice.game_service.dto;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta generada al finalizar una partida, incluyendo el puntaje obtenido y el nuevo puntaje global del usuario")
public record FinalizarPartidaResponse(

        @Schema(
                description = "ID único de la partida que fue finalizada",
                example = "120"
        )
        Long partidaId,

        @Schema(
                description = "Puntaje obtenido por el usuario en esta partida",
                example = "8"
        )
        int puntajeObtenido,

        @Schema(
                description = "Puntaje global del usuario antes de sumar el resultado de esta partida",
                example = "1500"
        )
        int puntajeAnteriorGlobal,

        @Schema(
                description = "Puntaje global actualizado luego de agregar el puntaje obtenido en esta partida",
                example = "1508"
        )
        int puntajeNuevoGlobal,

        @Schema(
                description = "Fecha y hora en que se dio por terminada la partida",
                example = "2025-12-01T22:15:30"
        )
        LocalDateTime fechaFin,

        @Schema(
                description = "Mensaje que resume el resultado final o estado de la partida",
                example = "¡Buen trabajo! Tu puntaje ha sido actualizado."
        )
        String mensaje

) {}
