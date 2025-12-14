package com.microservice.auth_service.microservice_auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta que representa un usuario registrado en el sistema")
public class UsuarioResponse {

    @Schema(
        description = "ID único del usuario",
        example = "5"
    )
    private Long id;

    @Schema(
        description = "Nombre completo del usuario",
        example = "Carlos Pérez"
    )
    private String nombre;

    @Schema(
        description = "Correo electrónico del usuario",
        example = "carlos.perez@example.com"
    )
    private String correo;

    @Schema(
        description = "Rol asignado al usuario",
        example = "ADMIN"
    )
    private String rol;

    @Schema(
        description = "Estado actual del usuario",
        example = "ACTIVO"
    )
    private String estado;

    @Schema(
        description = "Puntaje actual del usuario dentro del sistema",
        example = "50"
    )
    private Integer puntaje;

    @Schema(
        description = "Puntaje acumulado total del usuario",
        example = "1200"
    )
    private Integer puntajeGlobal;

    @Schema(
        description = "Foto de perfil codificada en Base64",
        example = "iVBORw0KGgoAAAANSUhEUgAAAOE..."
    )
    private String fotoPerfilBase64;

    public UsuarioResponse() {
    }

    public UsuarioResponse(Long id, String nombre, String correo,
                           String rol, String estado,
                           Integer puntaje, Integer puntajeGlobal,
                           String fotoPerfilBase64) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
        this.estado = estado;
        this.puntaje = puntaje;
        this.puntajeGlobal = puntajeGlobal;
        this.fotoPerfilBase64 = fotoPerfilBase64;
    }

    // Getters y setters

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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }

    public Integer getPuntajeGlobal() {
        return puntajeGlobal;
    }

    public void setPuntajeGlobal(Integer puntajeGlobal) {
        this.puntajeGlobal = puntajeGlobal;
    }

    public String getFotoPerfilBase64() {
        return fotoPerfilBase64;
    }

    public void setFotoPerfilBase64(String fotoPerfilBase64) {
        this.fotoPerfilBase64 = fotoPerfilBase64;
    }
}

