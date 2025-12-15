package com.microservice.quiz_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta que representa una opción de una pregunta")
public class OpcionResponse {

    @Schema(
            description = "ID de la opción",
            example = "10"
    )
    private Long id;

    @Schema(
            description = "Texto visible de la opción",
            example = "París"
    )
    private String texto;

    @Schema(
            description = "Indica si la opción es correcta",
            example = "true"
    )
    private boolean correcta;

    public OpcionResponse() {}

    public OpcionResponse(Long id, String texto, boolean correcta) {
        this.id = id;
        this.texto = texto;
        this.correcta = correcta;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public boolean isCorrecta() { return correcta; }
    public void setCorrecta(boolean correcta) { this.correcta = correcta; }
}
