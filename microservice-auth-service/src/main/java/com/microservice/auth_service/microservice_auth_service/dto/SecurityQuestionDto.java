package com.microservice.auth_service.microservice_auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Pregunta de seguridad disponible para selección")
public record SecurityQuestionDto(

        @Schema(description = "ID de la pregunta", example = "1")
        Integer id,

        @Schema(description = "Texto visible de la pregunta", example = "¿Cuál es el nombre de tu primera mascota?")
        String texto
) {}
