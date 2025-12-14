package com.microservice.quiz_service.repository;

import com.microservice.quiz_service.model.Dificultad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DificultadRepository extends JpaRepository<Dificultad, Long> {
}
