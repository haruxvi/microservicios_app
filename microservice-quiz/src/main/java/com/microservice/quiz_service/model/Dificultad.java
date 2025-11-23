package com.microservice.quiz_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Dificultad")
public class Dificultad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dificultad")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    public Dificultad() {
    }

    public Dificultad(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }
}