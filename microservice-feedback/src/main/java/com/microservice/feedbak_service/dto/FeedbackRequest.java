// dto/FeedbackRequest.java
package com.example.quizapp.feedback.dto;

import jakarta.validation.constraints.*;

public record FeedbackRequest(
    @NotNull(message = "El ID del usuario es obligatorio")
    Long usuarioId,

    @NotBlank(message = "El mensaje no puede estar vacío")
    @Size(max = 255, message = "El mensaje no puede tener más de 255 caracteres")
    String mensaje,

    @NotBlank(message = "El tipo es obligatorio")
    @Pattern(regexp = "BUG|MEJORA|SUGERENCIA|OTRO", message = "Tipo inválido")
    String tipo,

    @NotBlank(message = "El destino es obligatorio")
    @Pattern(regexp = "APP|JUEGO|PREGUNTAS|OTRO", message = "Destino inválido")
    String destino
) {}