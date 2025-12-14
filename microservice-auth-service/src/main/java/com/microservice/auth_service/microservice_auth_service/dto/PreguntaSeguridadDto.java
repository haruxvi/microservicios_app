package com.microservice.auth_service.microservice_auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Pregunta de seguridad asociada a un usuario para recuperación de contraseña")
public record PreguntaSeguridadDto(

        @Schema(description = "ID de la pregunta", example = "3")
        Integer id,

        @Schema(description = "Texto de la pregunta", example = "¿En qué ciudad naciste?")
        String texto
) {}
