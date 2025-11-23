package com.microservice.auth_service.microservice_auth_service.controller;

import com.microservice.auth_service.microservice_auth_service.dto.RolRequest;
import com.microservice.auth_service.microservice_auth_service.dto.RolResponse;
import com.microservice.auth_service.microservice_auth_service.service.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/roles")
@Tag(name = "Roles", description = "Gestión de roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @Operation(summary = "Listado de roles")
    @GetMapping
    public ResponseEntity<List<RolResponse>> getAll() {
        return ResponseEntity.ok(rolService.getAll());
    }

    @Operation(summary = "Crear rol")
    @PostMapping
    public ResponseEntity<RolResponse> create(@RequestBody RolRequest request) {
        return ResponseEntity.ok(rolService.create(request));
    }
}
