package com.microservice.auth_service.microservice_auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Schema(description = "Petición para verificar respuestas de preguntas de seguridad y obtener un token de recuperación")
public record VerifyRecoveryRequest(

        @Schema(description = "ID del usuario que intenta recuperar su contraseña", example = "12")
        @NotNull(message = "El userId es obligatorio")
        Long userId,

        @Schema(description = "Listado de respuestas a las preguntas (debe contener 3 ítems)")
        @NotEmpty(message = "Debe enviar al menos una respuesta")
        @Valid
        List<Item> items
) {
    @Schema(description = "Respuesta del usuario a una pregunta de seguridad")
    public record Item(

            @Schema(description = "ID de la pregunta de seguridad", example = "3")
            @NotNull(message = "El preguntaId es obligatorio")
            Integer preguntaId,

            @Schema(description = "Respuesta asociada a la pregunta", example = "Mi primera mascota se llamaba Toby")
            @NotBlank(message = "La respuesta no puede estar vacía")
            String respuesta
    ) {}
}
