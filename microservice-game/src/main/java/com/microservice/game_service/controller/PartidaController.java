package com.microservice.game_service.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.microservice.game_service.dto.FinalizarPartidaRequest;
import com.microservice.game_service.dto.IniciarPartidaRequest;
import com.microservice.game_service.dto.IniciarPartidaResponse;
import com.microservice.game_service.service.PartidaService;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
@Tag(name = "Game Service", description = "Orquestación del juego y partidas")
public class PartidaController {

    private final PartidaService partidaService;

    @PostMapping("/iniciar")
    @Operation(summary = "Iniciar una nueva partida")
    public ResponseEntity<IniciarPartidaResponse> iniciar(@RequestBody IniciarPartidaRequest request) {
        return ResponseEntity.ok(partidaService.iniciarPartida(request));
    }

    @PostMapping("/finalizar")
    @Operation(summary = "Finalizar partida y actualizar puntaje")
    public ResponseEntity<String> finalizar(@RequestBody FinalizarPartidaRequest request) {
        return ResponseEntity.ok(partidaService.finalizarPartida(request));
    }
}