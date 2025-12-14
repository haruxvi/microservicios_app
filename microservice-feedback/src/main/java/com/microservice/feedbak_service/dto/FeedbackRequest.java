package com.microservice.feedbak_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Petición para crear un nuevo feedback enviado por un usuario")
public record FeedbackRequest(

    @Schema(description = "ID del usuario que envía el feedback", example = "1")
    @NotNull Long usuarioId,

    @Schema(description = "Mensaje o descripción del feedback", 
            example = "Sería bueno agregar más preguntas de historia.")
    @NotBlank @Size(max = 255) String mensaje,

    @Schema(description = "Tipo de feedback (ej: ERROR, SUGERENCIA, OTRO)", 
            example = "SUGERENCIA")
    @NotBlank String tipo,

    @Schema(description = "Sección o módulo al cual va dirigido el feedback",
            example = "QUIZ")
    @NotBlank String destino
) {}
