package com.microservice.quiz_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Estado")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    public Estado() {
    }

    public Estado(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }
}