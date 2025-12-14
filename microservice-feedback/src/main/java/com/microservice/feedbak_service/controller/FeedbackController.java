package com.microservice.feedbak_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.microservice.feedbak_service.dto.FeedbackRequest;
import com.microservice.feedbak_service.dto.FeedbackResponse;
import com.microservice.feedbak_service.service.FeedbackService;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback", description = "Gestión de reportes, sugerencias y comentarios enviados por los usuarios")
public class FeedbackController {

    private final FeedbackService service;

    @Operation(
            summary = "Crear un nuevo feedback",
            description = "Permite que un usuario envíe un reporte, sugerencia o comentario. "
                        + "Devuelve el feedback creado con su información."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Feedback creado correctamente",
                    content = @Content(schema = @Schema(implementation = FeedbackResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<FeedbackResponse> crear(
            @Valid @RequestBody FeedbackRequest req
    ) {
        return ResponseEntity.ok(service.crear(req));
    }

    @Operation(
            summary = "Obtener todos los feedback",
            description = "Devuelve una lista con todos los feedback registrados, "
                        + "incluyendo resueltos y pendientes."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente",
                    content = @Content(array = @ArraySchema(
                            schema = @Schema(implementation = FeedbackResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping
    public List<FeedbackResponse> todos() {
        return service.todos();
    }

    @Operation(
            summary = "Obtener feedback pendientes",
            description = "Devuelve todos los feedback que aún no han sido marcados como resueltos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de pendientes obtenido correctamente",
                    content = @Content(array = @ArraySchema(
                            schema = @Schema(implementation = FeedbackResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping("/pendientes")
    public List<FeedbackResponse> pendientes() {
        return service.pendientes();
    }

    @Operation(
            summary = "Obtener feedback de un usuario específico",
            description = "Devuelve todos los feedback enviados por un usuario identificado por su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de feedback del usuario obtenido correctamente",
                    content = @Content(array = @ArraySchema(
                            schema = @Schema(implementation = FeedbackResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping("/usuario/{usuarioId}")
    public List<FeedbackResponse> porUsuario(
            @Parameter(description = "ID del usuario del cual obtener sus feedback", example = "1")
            @PathVariable Long usuarioId
    ) {
        return service.porUsuario(usuarioId);
    }

    @Operation(
            summary = "Marcar feedback como resuelto",
            description = "Cambia el estado del feedback indicado a RESUELTO y devuelve el feedback actualizado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Feedback resuelto correctamente",
                    content = @Content(schema = @Schema(implementation = FeedbackResponse.class))),
            @ApiResponse(responseCode = "404", description = "Feedback no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PatchMapping("/{id}/resolver")
    public ResponseEntity<FeedbackResponse> resolver(
            @Parameter(description = "ID del feedback a marcar como resuelto", example = "10")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.resolver(id));
    }

}
