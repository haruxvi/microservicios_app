package com.microservice.auth_service.microservice_auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Petición para crear o actualizar un estado de usuario")
public class EstadoRequest {

    @Schema(
        description = "Nombre del estado (ej. ACTIVO, INACTIVO, SUSPENDIDO)",
        example = "ACTIVO"
    )
    @NotBlank(message = "El nombre del estado no puede estar vacío")
    private String nombre;

    public EstadoRequest() {
    }

    public EstadoRequest(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
