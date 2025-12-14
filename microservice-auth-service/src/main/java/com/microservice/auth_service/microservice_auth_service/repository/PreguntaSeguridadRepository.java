package com.microservice.auth_service.microservice_auth_service.repository;

import com.microservice.auth_service.microservice_auth_service.model.PreguntaSeguridad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreguntaSeguridadRepository extends JpaRepository<PreguntaSeguridad, Integer> {
    List<PreguntaSeguridad> findByActivaTrue();
}
