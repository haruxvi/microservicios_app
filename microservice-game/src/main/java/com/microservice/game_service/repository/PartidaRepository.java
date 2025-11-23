package com.microservice.game_service.repository;

import java.util.List;
import java.util.Optional;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.microservice.game_service.model.Partida;


@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {
    @Query("SELECT p FROM Partida p WHERE p.usuarioId = :usuarioId ORDER BY p.fCreacion DESC")
    List<Partida> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT p FROM Partida p WHERE p.idPartida = :idPartida AND p.estadoId = 1")
    Optional<Partida> findActiveById(@Param("idPartida") Long idPartida);
}