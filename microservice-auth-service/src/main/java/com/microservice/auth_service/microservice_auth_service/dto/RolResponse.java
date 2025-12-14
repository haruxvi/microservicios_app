package com.microservice.auth_service.microservice_auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta que representa un rol registrado en el sistema")
public class RolResponse {

    @Schema(
        description = "ID único del rol",
        example = "1"
    )
    private Long id;

    @Schema(
        description = "Nombre del rol (ej. ADMIN, USER, INVITADO)",
        example = "ADMIN"
    )
    private String nombre;

    public RolResponse() {
    }

    public RolResponse(Long id, String nombre) {
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
