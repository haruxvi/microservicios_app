package com.microservice.auth_service.microservice_auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Petición para crear o actualizar un rol dentro del sistema")
public class RolRequest {

    @Schema(
        description = "Nombre del rol (ej. ADMIN, USER, INVITADO)",
        example = "ADMIN"
    )
    @NotBlank(message = "El nombre del rol no puede estar vacío")
    private String nombre;

    public RolRequest() {
    }

    public RolRequest(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
