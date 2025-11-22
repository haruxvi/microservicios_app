// dto/FeedbackResponse.java
package com.example.quizapp.feedback.dto;

import java.time.LocalDateTime;

public record FeedbackResponse(
    Long id,
    String mensaje,
    String tipo,
    String destino,
    LocalDateTime fecha,
    boolean resuelto,
    Long usuarioId
) {}