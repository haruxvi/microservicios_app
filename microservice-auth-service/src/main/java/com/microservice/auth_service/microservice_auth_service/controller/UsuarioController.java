package com.microservice.auth_service.microservice_auth_service.controller;

import com.microservice.auth_service.microservice_auth_service.dto.UsuarioRequest;
import com.microservice.auth_service.microservice_auth_service.dto.UsuarioResponse;
import com.microservice.auth_service.microservice_auth_service.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Listado de usuarios")
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> getAll() {
        return ResponseEntity.ok(usuarioService.getAll());
    }

    @Operation(summary = "Obtener usuario por id")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.getById(id));
    }

    @Operation(summary = "Crear usuario")
    @PostMapping
    public ResponseEntity<UsuarioResponse> create(@RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.create(request));
    }

    @Operation(summary = "Actualizar usuario")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> update(@PathVariable Long id,
                                                  @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.update(id, request));
    }

    @Operation(summary = "Eliminar usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
