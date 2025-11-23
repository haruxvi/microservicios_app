package com.microservice.game_service.dto;

import jakarta.validation.constraints.NotNull;

public record IniciarPartidaRequest(
        @NotNull(message = "Usuario ID es obligatorio")
        Long usuarioId,

        @NotNull(message = "Categoría ID es obligatoria")
        Long categoriaId,

        @NotNull(message = "Dificultad ID es obligatoria")
        Long dificultadId
) {}
