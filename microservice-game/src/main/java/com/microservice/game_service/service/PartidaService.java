package com.microservice.game_service.service;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.microservice.game_service.dto.IniciarPartidaRequest;
import com.microservice.game_service.dto.IniciarPartidaResponse;
import com.microservice.game_service.dto.PreguntaResponse;
import com.microservice.game_service.dto.FinalizarPartidaRequest;
import com.microservice.game_service.model.Partida;
import com.microservice.game_service.repository.PartidaRepository;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;





@Service
@RequiredArgsConstructor
@Transactional


public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final QuizClient quizClient;
    private final AuthClient authClient;

    public IniciarPartidaResponse iniciarPartida(IniciarPartidaRequest request) {
        Partida partida = new Partida();
        partida.setFCreacion(LocalDateTime.now());
        partida.setUsuarioId(request.usuarioId());
        partida.setCategoriaId(request.categoriaId());
        partida.setDificultadId(request.dificultadId());

        Partida saved = partidaRepository.save(partida);

        List<PreguntaResponse> preguntas = quizClient.obtenerPreguntas(
            request.categoriaId(), request.dificultadId()
        );

        return new IniciarPartidaResponse(
            saved.getIdPartida() != null ? saved.getIdPartida().longValue() : null,
            saved.getFCreacion(),
            preguntas
        );
    }

    @SuppressWarnings("null")
    public String finalizarPartida(FinalizarPartidaRequest request) {
        Partida partida = partidaRepository.findById(request.partidaId())
            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
        partida.setFFinalizacion(LocalDateTime.now());
        partida.setPuntTotal(request.puntajeObtenido());

        partidaRepository.save(partida);

        // convertir usuarioId a Long si es Integer
        authClient.sumarPuntaje(partida.getUsuarioId(), request.puntajeObtenido());

        return "Partida finalizada con puntaje: " + request.puntajeObtenido();
    }
}