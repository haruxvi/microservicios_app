package com.microservice.quiz_service.dto;

public class DificultadRequest {
    private String nombre;
    public DificultadRequest() {}
    public DificultadRequest(String nombre) { this.nombre = nombre; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}