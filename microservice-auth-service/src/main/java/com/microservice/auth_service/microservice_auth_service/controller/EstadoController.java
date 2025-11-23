package com.microservice.auth_service.microservice_auth_service.controller;

import com.microservice.auth_service.microservice_auth_service.dto.EstadoRequest;
import com.microservice.auth_service.microservice_auth_service.dto.EstadoResponse;
import com.microservice.auth_service.microservice_auth_service.service.EstadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/estados")
@Tag(name = "Estados", description = "Gestión de estados de usuario")
public class EstadoController {

    private final EstadoService estadoService;

    public EstadoController(EstadoService estadoService) {
        this.estadoService = estadoService;
    }

    @Operation(summary = "Listado de estados")
    @GetMapping
    public ResponseEntity<List<EstadoResponse>> getAll() {
        return ResponseEntity.ok(estadoService.getAll());
    }

    @Operation(summary = "Crear estado")
    @PostMapping
    public ResponseEntity<EstadoResponse> create(@RequestBody EstadoRequest request) {
        return ResponseEntity.ok(estadoService.create(request));
    }
}
