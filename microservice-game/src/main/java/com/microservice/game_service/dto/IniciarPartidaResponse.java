package com.microservice.game_service.dto;

import java.time.LocalDateTime;
import java.util.List;


public record IniciarPartidaResponse(
    Long partidaId,
    LocalDateTime fechaInicio,
    List<PreguntaResponse> preguntas
) {}    