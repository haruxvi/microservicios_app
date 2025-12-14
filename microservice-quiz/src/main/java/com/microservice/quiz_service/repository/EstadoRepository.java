package com.microservice.quiz_service.repository;

import com.microservice.quiz_service.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoRepository extends JpaRepository<Estado, Long> {
}
