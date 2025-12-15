package com.microservice.game_service.repository;

import com.microservice.game_service.model.Partida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartidaRepository extends JpaRepository<Partida, Long> {

    List<Partida> findByUsuarioId(Long usuarioId);
}
