package com.microservice.game_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Dificultad")
public class Dificultad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dificultad")
    private Long idDificultad;

    @Column(name = "nombre_dificultad", nullable = false)
    private String nombreDificultad;

    @Column(name = "tiempo_seg", nullable = false)
    private LocalTime tiempoSeg;

    @Column(name = "multip_punt", nullable = false)
    private Integer multipPunt;
}
package com.microservice.game_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Dificultad")
public class Dificultad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDificultad;

    private String nivel;
}
