package com.microservice.auth_service.microservice_auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Petición para restablecer la contraseña usando un token temporal de recuperación")
public record ResetPasswordRequest(

        @Schema(
                description = "Token temporal emitido al verificar las respuestas",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.recovery.abc123"
        )
        @NotBlank(message = "El token no puede estar vacío")
        String token,

        @Schema(
                description = "Nueva contraseña del usuario",
                example = "NuevaClave#2025"
        )
        @NotBlank(message = "La nueva contraseña no puede estar vacía")
        @Size(min = 8, message = "La nueva contraseña debe tener al menos 8 caracteres")
        String newPassword
) {}
