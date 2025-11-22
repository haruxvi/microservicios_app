
package com.example.quizapp.feedback.dto;

public record FeedbackResponse(
    Long id,
    String mensaje,
    String tipo,
    String destino,
    String fecha,
    boolean resuelto,
    Long usuarioId
) {}