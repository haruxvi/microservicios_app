package com.microservice.quiz_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Respuesta que representa una pregunta con sus opciones")
public class PreguntaResponse {

    @Schema(
            description = "ID de la pregunta",
            example = "5"
    )
    private Long id;

    @Schema(
            description = "Enunciado de la pregunta",
            example = "¿Cuál es la capital de Francia?"
    )
    private String enunciado;

    @Schema(
            description = "Nombre de la categoría",
            example = "Geografía"
    )
    private String categoria;

    @Schema(
            description = "Nivel de dificultad",
            example = "Media"
    )
    private String dificultad;

    @Schema(
            description = "Estado de la pregunta",
            example = "ACTIVA"
    )
    private String estado;

    @Schema(
            description = "Listado de opciones de la pregunta"
    )
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
