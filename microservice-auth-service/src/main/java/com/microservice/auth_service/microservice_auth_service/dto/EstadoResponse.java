package com.microservice.auth_service.microservice_auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta que representa un estado de usuario registrado en el sistema")
public class EstadoResponse {

    @Schema(
        description = "ID único del estado",
        example = "1"
    )
    private Long id;

    @Schema(
        description = "Nombre del estado (ej. ACTIVO, INACTIVO, SUSPENDIDO)",
        example = "ACTIVO"
    )
    private String nombre;

    public EstadoResponse() {
    }

    public EstadoResponse(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
