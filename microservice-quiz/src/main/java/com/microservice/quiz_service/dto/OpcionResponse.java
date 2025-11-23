package com.microservice.quiz_service.dto;

public class OpcionResponse {
    private Long id;
    private String texto;
    private boolean esCorrecta;

    public OpcionResponse() {}

    public OpcionResponse(Long id, String texto, boolean esCorrecta) {
        this.id = id; this.texto = texto; this.esCorrecta = esCorrecta;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public boolean isEsCorrecta() { return esCorrecta; }
    public void setEsCorrecta(boolean esCorrecta) { this.esCorrecta = esCorrecta; }
}