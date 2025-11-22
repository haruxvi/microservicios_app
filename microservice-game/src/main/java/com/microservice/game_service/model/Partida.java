package com.microservice.game_service.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Partida")
@Getter @Setter
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
}