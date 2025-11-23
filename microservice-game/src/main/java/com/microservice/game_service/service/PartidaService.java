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
            java.util.Objects.requireNonNull(saved.getIdPartida(), "idPartida nulo tras persistir"),
            saved.getFCreacion(),
            preguntas
        );
    }

    
    public com.microservice.game_service.dto.FinalizarPartidaResponse finalizarPartida(FinalizarPartidaRequest request) {
        Partida partida = partidaRepository.findById(request.partidaId())
            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
        partida.setFFinalizacion(LocalDateTime.now());
        partida.setPuntTotal(request.puntajeObtenido());

        // obtener puntaje anterior del usuario
        int puntajeAnterior = authClient.obtenerPuntajeActual(partida.getUsuarioId());

        // persistir la partida con el puntaje final
        Partida saved = partidaRepository.save(partida);

        // sumar puntaje al usuario en auth-service
        authClient.sumarPuntaje(partida.getUsuarioId(), request.puntajeObtenido());

        int puntajeNuevoTotal = puntajeAnterior + request.puntajeObtenido();

        return new com.microservice.game_service.dto.FinalizarPartidaResponse(
            java.util.Objects.requireNonNull(saved.getIdPartida(), "idPartida nulo tras persistir"),
            request.puntajeObtenido(),
            puntajeAnterior,
            puntajeNuevoTotal,
            saved.getFFinalizacion(),
            "Partida finalizada exitosamente"
        );
    }
}


