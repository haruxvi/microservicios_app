package com.microservice.game_service.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Partida")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_partida")
    private Long idPartida;

    @Column(name = "f_creacion", nullable = false)
    private LocalDateTime fCreacion;

    @Column(name = "f_finalizacion")
    private LocalDateTime fFinalizacion;

    @Column(name = "punt_total", nullable = false)
    private int puntTotal;

    @Column(name = "Estado_id_estado", nullable = false)
    private Long estadoId;

    @Column(name = "Usuario_id_usuario", nullable = false)
    private Long usuarioId;

    @Column(name = "Categoria_id_categoria", nullable = false)
    private Long categoriaId;

    @Column(name = "Dificultad_id_dificultad", nullable = false)
    private Long dificultadId;

    public Partida() {
    }

    public Partida(Long usuarioId, Long categoriaId, Long dificultadId, Long estadoId) {
        this.usuarioId = usuarioId;
        this.categoriaId = categoriaId;
        this.dificultadId = dificultadId;
        this.estadoId = estadoId;
        this.fCreacion = LocalDateTime.now();
        this.puntTotal = 0;
    }

    public Long getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(Long idPartida) {
        this.idPartida = idPartida;
    }

    public LocalDateTime getFCreacion() {
        return fCreacion;
    }

    public void setFCreacion(LocalDateTime fCreacion) {
        this.fCreacion = fCreacion;
    }

    public LocalDateTime getFFinalizacion() {
        return fFinalizacion;
    }

    public void setFFinalizacion(LocalDateTime fFinalizacion) {
        this.fFinalizacion = fFinalizacion;
    }

    public int getPuntTotal() {
        return puntTotal;
    }

    public void setPuntTotal(int puntTotal) {
        this.puntTotal = puntTotal;
    }

    public Long getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Long estadoId) {
        this.estadoId = estadoId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Long getDificultadId() {
        return dificultadId;
    }

    public void setDificultadId(Long dificultadId) {
        this.dificultadId = dificultadId;
    }
}
