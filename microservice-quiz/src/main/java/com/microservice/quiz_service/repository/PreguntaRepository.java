package com.microservice.quiz_service.repository;

import com.microservice.quiz_service.model.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {

    List<Pregunta> findByCategoria_Id(Long idCategoria);

    List<Pregunta> findByDificultad_Id(Long idDificultad);

    List<Pregunta> findByCategoria_IdAndDificultad_Id(Long idCategoria, Long idDificultad);
}
