package com.microservice.game_service.service;


import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.microservice.game_service.model.Partida;
import com.microservice.game_service.repository.PartidaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;






@Service
@RequiredArgsConstructor
public class PartidaService {
    private final PartidaRepository partidaRepository;
    private final QuizClient quizClient;
    private final AuthClient authClient;

    @Transactional
    public IniciarPartidaResponse iniciarPartida(IniciarPartidaRequest request) {
        // Crear partida
        Partida partida = new Partida();
        partida.setUsuarioId(request.usuarioId());
        partida.setCategoriaId(request.categoriaId());
        partida.setDificultadId(request.dificultadId());
        partida.setFechaInicio(LocalDateTime.now());
        partida.setEstado("EN_CURSO");

        Partida guardada = partidaRepository.save(partida);

        // Obtener preguntas de quiz-service
        List<PreguntaResponse> preguntas = quizClient.obtenerPreguntas(
            request.categoriaId(), request.dificultadId()
        );

        return new IniciarPartidaResponse(
            guardada.getId(),
            preguntas,
            guardada.getFechaInicio()
        );
    }

    @Transactional
    public String finalizarPartida(FinalizarPartidaRequest request) {
        Partida partida = partidaRepository.findById(request.partidaId())
            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));

        partida.setPuntajeFinal(request.puntajeObtenido());
        partida.setFechaFin(LocalDateTime.now());
        partida.setEstado("FINALIZADA");

        partidaRepository.save(partida);

        // Actualizar puntaje global del usuario
        authClient.sumarPuntaje(partida.getUsuarioId(), request.puntajeObtenido());

        return "Partida finalizada con éxito. Puntaje: " + request.puntajeObtenido();
    }

 

   
}