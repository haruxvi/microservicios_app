package com.microservice.quiz_service.dto;

import java.util.List;

public class PreguntaResponse {
    private Long id;
    private String enunciado;
    private String categoria;
    private String dificultad;
    private String estado;
    private List<OpcionResponse> opciones;

    public PreguntaResponse() {}

    public PreguntaResponse(Long id, String enunciado, String categoria,
                            String dificultad, String estado,
                            List<OpcionResponse> opciones) {
        this.id = id;
        this.enunciado = enunciado;
        this.categoria = categoria;
        this.dificultad = dificultad;
        this.estado = estado;
        this.opciones = opciones;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getDificultad() { return dificultad; }
    public void setDificultad(String dificultad) { this.dificultad = dificultad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<OpcionResponse> getOpciones() { return opciones; }
    public void setOpciones(List<OpcionResponse> opciones) { this.opciones = opciones; }
}