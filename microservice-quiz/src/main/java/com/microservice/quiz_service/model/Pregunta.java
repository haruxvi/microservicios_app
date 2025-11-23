package com.microservice.quiz_service.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Pregunta")
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta")
    private Long id;

    @Column(name = "enunciado", nullable = false, length = 500)
    private String enunciado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Categoria_id_categoria", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Dificultad_id_dificultad", nullable = false)
    private Dificultad dificultad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Estado_id_estado", nullable = false)
    private Estado estado;

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Opcion> opciones = new ArrayList<>();

    public Pregunta() {
    }

    public Pregunta(String enunciado, Categoria categoria, Dificultad dificultad, Estado estado) {
        this.enunciado = enunciado;
        this.categoria = categoria;
        this.dificultad = dificultad;
        this.estado = estado;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getEnunciado() { return enunciado; }

    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }

    public Categoria getCategoria() { return categoria; }

    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public Dificultad getDificultad() { return dificultad; }

    public void setDificultad(Dificultad dificultad) { this.dificultad = dificultad; }

    public Estado getEstado() { return estado; }

    public void setEstado(Estado estado) { this.estado = estado; }

    public List<Opcion> getOpciones() { return opciones; }

    public void setOpciones(List<Opcion> opciones) { this.opciones = opciones; }

    public void addOpcion(Opcion opcion) {
        opcion.setPregunta(this);
        this.opciones.add(opcion);
    }
}