package com.microservice.feedbak_service.dto;

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
