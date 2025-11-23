package com.microservice.auth_service.microservice_auth_service.dto;

public class UsuarioResponse {

    private Long id;
    private String nombre;
    private String correo;
    private String rol;
    private String estado;
    private Integer puntaje;
    private Integer puntajeGlobal;

    public UsuarioResponse() {
    }

    public UsuarioResponse(Long id, String nombre, String correo,
                           String rol, String estado,
                           Integer puntaje, Integer puntajeGlobal) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
        this.estado = estado;
        this.puntaje = puntaje;
        this.puntajeGlobal = puntajeGlobal;
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
}
