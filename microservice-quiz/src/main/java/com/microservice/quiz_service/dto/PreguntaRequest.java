package com.microservice.quiz_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Petición para crear o actualizar una pregunta del sistema")
public class PreguntaRequest {

    @Schema(
            description = "Enunciado de la pregunta",
            example = "¿Cuál es la capital de Francia?"
    )
    @NotBlank(message = "El enunciado no puede estar vacío")
    private String enunciado;

    @Schema(
            description = "ID de la categoría asociada a la pregunta",
            example = "1"
    )
    @NotNull(message = "La categoría es obligatoria")
    private Long idCategoria;

    @Schema(
            description = "ID del nivel de dificultad de la pregunta",
            example = "2"
    )
    @NotNull(message = "La dificultad es obligatoria")
    private Long idDificultad;

    @Schema(
            description = "ID del estado de la pregunta (ej. ACTIVA, INACTIVA)",
            example = "1"
    )
    @NotNull(message = "El estado es obligatorio")
    private Long idEstado;

    @Schema(
            description = "Listado de opciones de respuesta de la pregunta"
    )
    @NotNull(message = "La pregunta debe tener opciones")
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
