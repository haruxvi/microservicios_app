package com.microservice.quiz_service.dto;

public class OpcionRequest {
    private String texto;
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