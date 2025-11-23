package com.microservice.game_service.dto;

import java.time.LocalDateTime;

public record FinalizarPartidaResponse(
        Long partidaId,
        int puntajeObtenido,
        int puntajeAnteriorGlobal,
        int puntajeNuevoGlobal,
        LocalDateTime fechaFin,
        String mensaje
) {}
