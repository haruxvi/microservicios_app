package com.microservice.auth_service.microservice_auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de verificación de recuperación: entrega un token temporal para restablecer la contraseña")
public record VerifyRecoveryResponse(
        @Schema(
                description = "Token temporal para autorizar el cambio de contraseña",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.recovery.abc123"
        )
        String token
) {}
