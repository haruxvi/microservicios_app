package com.microservice.quiz_service.controller;

import com.microservice.quiz_service.dto.PreguntaRequest;
import com.microservice.quiz_service.dto.PreguntaResponse;
import com.microservice.quiz_service.services.PreguntaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz/preguntas")
@Tag(name = "Preguntas", description = "Gestión de preguntas del quiz")
public class PreguntaController {

    private final PreguntaService preguntaService;

    public PreguntaController(PreguntaService preguntaService) {
        this.preguntaService = preguntaService;
    }

    @Operation(summary = "Listar todas las preguntas",
            description = "Devuelve todas las preguntas registradas en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente",
                    content = @Content(array = @ArraySchema(
                            schema = @Schema(implementation = PreguntaResponse.class)
                    ))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<PreguntaResponse>> getAll() {
        return ResponseEntity.ok(preguntaService.getAll());
    }

    @Operation(summary = "Obtener pregunta por id",
            description = "Devuelve una pregunta específica según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pregunta encontrada",
                    content = @Content(schema = @Schema(implementation = PreguntaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pregunta no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PreguntaResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(preguntaService.getById(id));
    }

    @Operation(summary = "Listar preguntas por categoría",
            description = "Devuelve todas las preguntas asociadas a una categoría específica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente",
                    content = @Content(array = @ArraySchema(
                            schema = @Schema(implementation = PreguntaResponse.class)
                    ))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<PreguntaResponse>> getByCategoria(@PathVariable Long idCategoria) {
        return ResponseEntity.ok(preguntaService.getByCategoria(idCategoria));
    }

    @Operation(summary = "Listar preguntas por dificultad",
            description = "Devuelve todas las preguntas asociadas a una dificultad específica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente",
                    content = @Content(array = @ArraySchema(
                            schema = @Schema(implementation = PreguntaResponse.class)
                    ))),
            @ApiResponse(responseCode = "404", description = "Dificultad no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping("/dificultad/{idDificultad}")
    public ResponseEntity<List<PreguntaResponse>> getByDificultad(@PathVariable Long idDificultad) {
        return ResponseEntity.ok(preguntaService.getByDificultad(idDificultad));
    }

    @Operation(summary = "Listar preguntas por categoría y dificultad",
            description = "Devuelve las preguntas filtradas por una categoría y una dificultad específicas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente",
                    content = @Content(array = @ArraySchema(
                            schema = @Schema(implementation = PreguntaResponse.class)
                    ))),
            @ApiResponse(responseCode = "404", description = "Categoría o dificultad no encontradas",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping("/categoria/{idCategoria}/dificultad/{idDificultad}")
    public ResponseEntity<List<PreguntaResponse>> getByCategoriaAndDificultad(
            @PathVariable Long idCategoria,
            @PathVariable Long idDificultad
    ) {
        return ResponseEntity.ok(
                preguntaService.getByCategoriaAndDificultad(idCategoria, idDificultad)
        );
    }

    @Operation(summary = "Crear una pregunta con opciones",
            description = "Crea una nueva pregunta junto con sus opciones asociadas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pregunta creada correctamente",
                    content = @Content(schema = @Schema(implementation = PreguntaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<PreguntaResponse> create(@RequestBody PreguntaRequest request) {
        return ResponseEntity.ok(preguntaService.create(request));
    }

    @Operation(summary = "Actualizar una pregunta con opciones",
            description = "Actualiza una pregunta existente y sus opciones asociadas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pregunta actualizada correctamente",
                    content = @Content(schema = @Schema(implementation = PreguntaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Pregunta no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PreguntaResponse> update(
            @PathVariable Long id,
            @RequestBody PreguntaRequest request
    ) {
        return ResponseEntity.ok(preguntaService.update(id, request));
    }

    @Operation(summary = "Eliminar pregunta",
            description = "Elimina una pregunta del sistema por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pregunta eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Pregunta no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        preguntaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
