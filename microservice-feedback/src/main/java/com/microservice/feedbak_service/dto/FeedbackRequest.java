// dto/FeedbackRequest.java
package com.example.quizapp.feedback.dto;

import jakarta.validation.constraints.*;

public record FeedbackRequest(
    @NotNull Long usuarioId,
    @NotBlank @Size(max = 255) String mensaje,
    @NotBlank String tipo,
    @NotBlank String destino
) {}