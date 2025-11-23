package com.microservice.feedbak_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FeedbackRequest(
    @NotNull Long usuarioId,
    @NotBlank @Size(max = 255) String mensaje,
    @NotBlank String tipo,
    @NotBlank String destino
) {}
