package com.microservice.quiz_service.repository;

import com.microservice.quiz_service.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
