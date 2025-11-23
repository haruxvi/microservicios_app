package com.microservice.quiz_service.controller;

import com.microservice.quiz_service.dto.PreguntaRequest;
import com.microservice.quiz_service.dto.PreguntaResponse;
import com.microservice.quiz_service.services.PreguntaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "Listar todas las preguntas")
    @GetMapping
    public ResponseEntity<List<PreguntaResponse>> getAll() {
        return ResponseEntity.ok(preguntaService.getAll());
    }

    @Operation(summary = "Obtener pregunta por id")
    @GetMapping("/{id}")
    public ResponseEntity<PreguntaResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(preguntaService.getById(id));
    }

    @Operation(summary = "Listar preguntas por categoría")
    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<PreguntaResponse>> getByCategoria(@PathVariable Long idCategoria) {
        return ResponseEntity.ok(preguntaService.getByCategoria(idCategoria));
    }

    @Operation(summary = "Listar preguntas por dificultad")
    @GetMapping("/dificultad/{idDificultad}")
    public ResponseEntity<List<PreguntaResponse>> getByDificultad(@PathVariable Long idDificultad) {
        return ResponseEntity.ok(preguntaService.getByDificultad(idDificultad));
    }

    @Operation(summary = "Listar preguntas por categoría y dificultad")
    @GetMapping("/categoria/{idCategoria}/dificultad/{idDificultad}")
    public ResponseEntity<List<PreguntaResponse>> getByCategoriaAndDificultad(
            @PathVariable Long idCategoria,
            @PathVariable Long idDificultad
    ) {
        return ResponseEntity.ok(
                preguntaService.getByCategoriaAndDificultad(idCategoria, idDificultad)
        );
    }

    @Operation(summary = "Crear una pregunta con opciones")
    @PostMapping
    public ResponseEntity<PreguntaResponse> create(@RequestBody PreguntaRequest request) {
        return ResponseEntity.ok(preguntaService.create(request));
    }

    @Operation(summary = "Eliminar pregunta")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        preguntaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
