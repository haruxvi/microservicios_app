package com.microservice.quiz_service.dto;

import java.util.List;

public class PreguntaRequest {
    private String enunciado;
    private Long idCategoria;
    private Long idDificultad;
    private Long idEstado;
    private List<OpcionRequest> opciones;

    public PreguntaRequest() {}

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }

    public Long getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Long idCategoria) { this.idCategoria = idCategoria; }

    public Long getIdDificultad() { return idDificultad; }
    public void setIdDificultad(Long idDificultad) { this.idDificultad = idDificultad; }

    public Long getIdEstado() { return idEstado; }
    public void setIdEstado(Long idEstado) { this.idEstado = idEstado; }

    public List<OpcionRequest> getOpciones() { return opciones; }
    public void setOpciones(List<OpcionRequest> opciones) { this.opciones = opciones; }
}