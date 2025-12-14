package com.microservice.game_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.microservice.game_service.dto.FinalizarPartidaRequest;
import com.microservice.game_service.dto.FinalizarPartidaResponse;
import com.microservice.game_service.dto.IniciarPartidaRequest;
import com.microservice.game_service.dto.IniciarPartidaResponse;
import com.microservice.game_service.dto.PartidaResponse;
import com.microservice.game_service.service.PartidaService;

import java.util.List;

@RestController
@RequestMapping("/api/game/partidas")
@Tag(name = "Partidas", description = "Gestión de partidas e historial de juego")
public class PartidaController {

    private final PartidaService partidaService;

    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    @Operation(
            summary = "Iniciar una nueva partida",
            description = "Crea una nueva partida con los datos enviados (usuario, categoría, dificultad) "
                        + "y devuelve la información inicial junto con las preguntas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Partida iniciada correctamente",
                    content = @Content(schema = @Schema(implementation = IniciarPartidaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario, categoría o dificultad no encontrados",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PostMapping("/iniciar")
    public ResponseEntity<IniciarPartidaResponse> iniciar(
            @Valid @RequestBody IniciarPartidaRequest request
    ) {
        return ResponseEntity.ok(partidaService.iniciarPartida(request));
    }

    @Operation(
            summary = "Finalizar una partida",
            description = "Registra el puntaje y finaliza una partida existente. "
                        + "Devuelve un resumen con los resultados finales."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Partida finalizada correctamente",
                    content = @Content(schema = @Schema(implementation = FinalizarPartidaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Partida no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PostMapping("/finalizar")
    public ResponseEntity<FinalizarPartidaResponse> finalizar(
            @Valid @RequestBody FinalizarPartidaRequest request
    ) {
        return ResponseEntity.ok(partidaService.finalizarPartida(request));
    }

    @Operation(
            summary = "Obtener historial de partidas de un usuario",
            description = "Devuelve todas las partidas jugadas por un usuario, incluyendo fecha y puntaje."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historial obtenido correctamente",
                    content = @Content(array = @ArraySchema(
                            schema = @Schema(implementation = PartidaResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PartidaResponse>> obtenerPartidasPorUsuario(
            @Parameter(description = "ID del usuario del cual se desea obtener el historial", example = "1")
            @PathVariable Long usuarioId
    ) {
        return ResponseEntity.ok(partidaService.obtenerPartidasPorUsuario(usuarioId));
    }
}
