package com.microservice.game_service.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa una pregunta de la partida, incluyendo su enunciado, puntaje y las opciones disponibles")
public record PreguntaResponse(

        @Schema(
                description = "ID único de la pregunta",
                example = "45"
        )
        Long id,

        @Schema(
                description = "Texto o enunciado de la pregunta",
                example = "¿Cuál es la unidad básica de la vida?"
        )
        String enunciado,

        @Schema(
                description = "Puntaje otorgado si el usuario responde correctamente esta pregunta",
                example = "2"
        )
        Integer puntaje,

        @Schema(
                description = "Listado de opciones de respuesta disponibles para esta pregunta",
                implementation = OpcionResponse.class
        )
        List<OpcionResponse> opciones

) {}

