package com.microservice.game_service.dto;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Petición para iniciar una nueva partida, indicando el usuario, la categoría y la dificultad seleccionada")
public record IniciarPartidaRequest(

        @Schema(
                description = "ID del usuario que desea iniciar la partida",
                example = "1"
        )
        @NotNull(message = "Usuario ID es obligatorio")
        Long usuarioId,

        @Schema(
                description = "ID de la categoría seleccionada para la partida",
                example = "3"
        )
        @NotNull(message = "Categoría ID es obligatoria")
        Long categoriaId,

        @Schema(
                description = "ID de la dificultad seleccionada",
                example = "2"
        )
        @NotNull(message = "Dificultad ID es obligatoria")
        Long dificultadId

) {}
