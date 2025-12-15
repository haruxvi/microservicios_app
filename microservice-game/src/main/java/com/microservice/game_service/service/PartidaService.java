package com.microservice.game_service.service;

import com.microservice.game_service.dto.*;
import com.microservice.game_service.model.Partida;
import com.microservice.game_service.repository.PartidaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final QuizClient quizClient;
    private final AuthClient authClient;

    // ID del estado "ACTIVA" (ajusta según tu tabla Estado)
    private static final long ESTADO_ACTIVA_ID = 1L;
    // ID del estado "FINALIZADA" (ajusta según tu tabla Estado)
    private static final long ESTADO_FINALIZADA_ID = 2L;

    public PartidaService(PartidaRepository partidaRepository,
                          QuizClient quizClient,
                          AuthClient authClient) {
        this.partidaRepository = partidaRepository;
        this.quizClient = quizClient;
        this.authClient = authClient;
    }

    public IniciarPartidaResponse iniciarPartida(IniciarPartidaRequest request) {
        // Crear Partida en estado ACTIVA con puntaje 0
        Partida partida = new Partida(
                request.usuarioId(),
                request.categoriaId(),
                request.dificultadId(),
                ESTADO_ACTIVA_ID
        );

        partida.setFCreacion(LocalDateTime.now());
        partida.setPuntTotal(0);

        Partida saved = partidaRepository.save(partida);

        // Obtener preguntas desde quiz-service
        List<PreguntaResponse> preguntas =
                quizClient.obtenerPreguntas(request.categoriaId(), request.dificultadId());

        return new IniciarPartidaResponse(
                saved.getIdPartida(),
                saved.getFCreacion(),
                preguntas
        );
    }

    public FinalizarPartidaResponse finalizarPartida(FinalizarPartidaRequest request) {
        Partida partida = partidaRepository.findById(request.partidaId())
                .orElseThrow(() -> new RuntimeException("Partida no encontrada"));

        // Obtener puntaje global anterior del usuario
        int puntajeAnterior = authClient.obtenerPuntajeGlobal(partida.getUsuarioId());

        // Sumamos el puntaje de esta partida al global
        int nuevoPuntajeGlobal = authClient.actualizarPuntajeGlobal(
                partida.getUsuarioId(),
                request.puntajeObtenido()
        );

        // Actualizar datos de la partida
        partida.setPuntTotal(request.puntajeObtenido());
        partida.setFFinalizacion(LocalDateTime.now());
        partida.setEstadoId(ESTADO_FINALIZADA_ID);

        Partida saved = partidaRepository.save(partida);

        return new FinalizarPartidaResponse(
                saved.getIdPartida(),
                request.puntajeObtenido(),
                puntajeAnterior,
                nuevoPuntajeGlobal,
                saved.getFFinalizacion(),
                "Partida finalizada exitosamente"
        );
    }

    public List<PartidaResponse> obtenerPartidasPorUsuario(Long usuarioId) {
        return partidaRepository.findByUsuarioId(usuarioId).stream()
                .map(this::toResponse)
                .toList();
    }

    private PartidaResponse toResponse(Partida p) {
        // Aquí podrías mapear IDs a nombres llamando a otros microservicios.
        // Por ahora devolvemos solo IDs y campos básicos.
        String categoria = "Categoria #" + p.getCategoriaId();
        String dificultad = "Dificultad #" + p.getDificultadId();
        String estado = "Estado #" + p.getEstadoId();

        return new PartidaResponse(
                p.getIdPartida(),
                p.getUsuarioId(),
                categoria,
                dificultad,
                p.getFCreacion(),
                p.getFFinalizacion(),
                p.getPuntTotal(),
                estado
        );
    }
}
