package com.microservice.game_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.game_service.model.Partida;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {
    List<Partida> findByUsuarioIdOrderByFechaInicioDesc(Long usuarioId);
}