package com.microservice.auth_service.microservice_auth_service.dto;

public class RolRequest {

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
