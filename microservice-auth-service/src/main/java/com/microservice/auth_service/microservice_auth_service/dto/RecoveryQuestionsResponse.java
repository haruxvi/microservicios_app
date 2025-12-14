package com.microservice.auth_service.microservice_auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Respuesta con las preguntas de seguridad configuradas para el usuario")
public record RecoveryQuestionsResponse(

        @Schema(description = "ID del usuario", example = "12")
        Long userId,

        @Schema(description = "Listado de preguntas configuradas (normalmente 3)")
        List<PreguntaSeguridadDto> questions
) {}
