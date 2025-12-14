package com.microservice.auth_service.microservice_auth_service.repository;

import com.microservice.auth_service.microservice_auth_service.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreoIgnoreCase(String correo);
    Optional<Usuario> findByNombreIgnoreCase(String nombre);

    boolean existsByCorreoIgnoreCase(String correo);
}
