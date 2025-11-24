package com.microservice.quiz_service.dto;

public class OpcionResponse {
    private Long id;
    private String texto;
    private boolean correcta;   // 👈 nombre unificado

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

    // 👇 este getter define el nombre del campo en el JSON: "correcta"
    public boolean isCorrecta() { return correcta; }
    public void setCorrecta(boolean correcta) { this.correcta = correcta; }
}
