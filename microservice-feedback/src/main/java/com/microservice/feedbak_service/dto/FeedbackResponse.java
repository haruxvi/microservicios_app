package com.microservice.feedbak_service.dto;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta que representa un feedback registrado en el sistema")
public record FeedbackResponse(

    @Schema(description = "ID del feedback", example = "10")
    Long id,

    @Schema(description = "Mensaje escrito por el usuario", 
            example = "La app se cierra cuando inicio un quiz.")
    String mensaje,

    @Schema(description = "Tipo de feedback (ERROR, SUGERENCIA, OTRO)", 
            example = "ERROR")
    String tipo,

    @Schema(description = "Módulo o sección al que está dirigido el feedback", 
            example = "QUIZ")
    String destino,

    @Schema(description = "Fecha y hora en que se creó el feedback", 
            example = "2025-11-30T15:23:00")
    LocalDateTime fecha,

    @Schema(description = "Indica si el feedback ya fue marcado como resuelto", 
            example = "false")
    boolean resuelto,

    @Schema(description = "ID del usuario que envió el feedback", 
            example = "1")
    Long usuarioId

) {}
