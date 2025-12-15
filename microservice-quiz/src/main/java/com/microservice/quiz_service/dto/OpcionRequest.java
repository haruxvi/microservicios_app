package com.microservice.quiz_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Petición para crear una opción de respuesta de una pregunta")
public class OpcionRequest {

    @Schema(
            description = "Texto visible de la opción",
            example = "París"
    )
    @NotBlank(message = "El texto de la opción no puede estar vacío")
    private String texto;

    @Schema(
            description = "Indica si la opción es correcta",
            example = "true"
    )
    private boolean esCorrecta;

    public OpcionRequest() {}

    public OpcionRequest(String texto, boolean esCorrecta) {
        this.texto = texto;
        this.esCorrecta = esCorrecta;
    }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public boolean isEsCorrecta() { return esCorrecta; }
    public void setEsCorrecta(boolean esCorrecta) { this.esCorrecta = esCorrecta; }
}
