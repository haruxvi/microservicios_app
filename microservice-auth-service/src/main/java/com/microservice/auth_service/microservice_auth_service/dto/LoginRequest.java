package com.microservice.auth_service.microservice_auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Petición para iniciar sesión en el sistema")
public record LoginRequest(

        @Schema(
                description = "Identificador del usuario, puede ser correo electrónico o nombre de usuario",
                example = "juan@example.com"
        )
        @NotBlank(message = "El identificador no puede estar vacío")
        String identificador,

        @Schema(
                description = "Clave del usuario en texto plano",
                example = "123456"
        )
        @NotBlank(message = "La clave no puede estar vacía")
        String clave
) {}
