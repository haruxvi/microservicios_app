package com.microservice.feedbak_service.repository;

import com.microservice.feedbak_service.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByResueltoFalseOrderByFechaDesc();

    List<Feedback> findByUsuarioIdOrderByFechaDesc(Long usuarioId);
}
