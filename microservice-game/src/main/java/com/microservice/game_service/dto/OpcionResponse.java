package com.microservice.game_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa una opción de respuesta perteneciente a una pregunta de la partida")
public record OpcionResponse(

        @Schema(
                description = "ID único de la opción",
                example = "12"
        )
        Long id,

        @Schema(
                description = "Texto visible de la opción",
                example = "La célula es la unidad básica de la vida"
        )
        String texto,

        @Schema(
                description = "Indica si esta opción es la respuesta correcta",
                example = "false"
        )
        boolean correcta

) {}

