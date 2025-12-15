package com.microservice.quiz_service.repository;

import com.microservice.quiz_service.model.Opcion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpcionRepository extends JpaRepository<Opcion, Long> {
}
