package com.microservice.auth_service.microservice_auth_service.repository;

import com.microservice.auth_service.microservice_auth_service.model.RespuestaSeguridad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RespuestaSeguridadRepository
        extends JpaRepository<RespuestaSeguridad, Long> {

    boolean existsByUsuarioId(Long usuarioId);
    List<RespuestaSeguridad> findByUsuarioId(Long usuarioId);


    void deleteByUsuarioId(Long usuarioId);
}

