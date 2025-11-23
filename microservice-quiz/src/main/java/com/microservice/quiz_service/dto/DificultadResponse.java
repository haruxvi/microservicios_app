package com.microservice.quiz_service.dto;

public class DificultadResponse {
    private Long id;
    private String nombre;

    public DificultadResponse() {}
    public DificultadResponse(Long id, String nombre) {
        this.id = id; this.nombre = nombre;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}