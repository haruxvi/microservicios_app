package com.microservice.quiz_service.dto;

public class EstadoRequest {
    private String nombre;
    public EstadoRequest() {}
    public EstadoRequest(String nombre) { this.nombre = nombre; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}