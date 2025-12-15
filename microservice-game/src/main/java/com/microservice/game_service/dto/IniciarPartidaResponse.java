package com.microservice.game_service.dto;

import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta generada al iniciar una nueva partida, incluyendo su ID, fecha de inicio y las preguntas asignadas")
public record IniciarPartidaResponse(

        @Schema(
                description = "ID único de la partida recién creada",
                example = "150"
        )
        Long partidaId,

        @Schema(
                description = "Fecha y hora en que se inició la partida",
                example = "2025-12-01T14:22:10"
        )
        LocalDateTime fechaInicio,

        @Schema(
                description = "Listado de preguntas generadas para esta partida",
                implementation = PreguntaResponse.class
        )
        List<PreguntaResponse> preguntas

) {}

