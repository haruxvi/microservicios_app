package com.microservice.auth_service.microservice_auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Petición para actualizar la foto de perfil del usuario")
public record ActualizarFotoPerfilRequest(
        @Schema(description = "Imagen codificada en Base64",
                example = "iVBORw0KGgoAAAANSUhEUgAA...")
        String fotoBase64
) {}

