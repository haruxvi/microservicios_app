package com.microservice.game_service.controller;




import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.microservice.game_service.dto.FinalizarPartidaRequest;
import com.microservice.game_service.dto.FinalizarPartidaResponse;
import com.microservice.game_service.dto.IniciarPartidaRequest;
import com.microservice.game_service.dto.IniciarPartidaResponse;
import com.microservice.game_service.service.PartidaService;


@RestController
@RequestMapping("/api/game")
@Tag(name = "Game Service", description = "Orquestador del juego")
public class PartidaController {

    private final PartidaService partidaService;

    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    @PostMapping("/iniciar")
    public ResponseEntity<IniciarPartidaResponse> iniciar(@Valid @RequestBody IniciarPartidaRequest request) {
        return ResponseEntity.ok(partidaService.iniciarPartida(request));
    }

    @PostMapping("/finalizar")
    public ResponseEntity<FinalizarPartidaResponse> finalizar(@Valid @RequestBody FinalizarPartidaRequest request) {
        return ResponseEntity.ok(partidaService.finalizarPartida(request));
    }
}
