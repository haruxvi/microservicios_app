package com.microservice.quiz_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Opciones")
public class Opcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_opcion")
    private Long id;

    @Column(name = "texto", nullable = false, length = 300)
    private String texto;

    @Column(name = "es_correcta", nullable = false)
    private boolean esCorrecta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Pregunta_id_pregunta", nullable = false)
    private Pregunta pregunta;

    public Opcion() {
    }

    public Opcion(String texto, boolean esCorrecta) {
        this.texto = texto;
        this.esCorrecta = esCorrecta;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTexto() { return texto; }

    public void setTexto(String texto) { this.texto = texto; }

    public boolean isEsCorrecta() { return esCorrecta; }

    public void setEsCorrecta(boolean esCorrecta) { this.esCorrecta = esCorrecta; }

    public Pregunta getPregunta() { return pregunta; }

    public void setPregunta(Pregunta pregunta) { this.pregunta = pregunta; }
}