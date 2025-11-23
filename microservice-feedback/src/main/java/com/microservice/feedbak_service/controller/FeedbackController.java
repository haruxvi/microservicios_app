package com.microservice.feedbak_service.controller;

import com.microservice.feedbak_service.dto.FeedbackRequest;
import com.microservice.feedbak_service.dto.FeedbackResponse;
import com.microservice.feedbak_service.service.FeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback", description = "Reportes y sugerencias de usuarios")
public class FeedbackController {

    private final FeedbackService service;

    @PostMapping
    public ResponseEntity<FeedbackResponse> crear(@Valid @RequestBody FeedbackRequest req) {
        return ResponseEntity.ok(service.crear(req));
    }

    @GetMapping
    public List<FeedbackResponse> todos() {
        return service.todos();
    }

    @GetMapping("/pendientes")
    public List<FeedbackResponse> pendientes() {
        return service.pendientes();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<FeedbackResponse> porUsuario(@PathVariable Long usuarioId) {
        return service.porUsuario(usuarioId);
    }

    @PatchMapping("/{id}/resolver")
    public ResponseEntity<FeedbackResponse> resolver(@PathVariable Long id) {
        return ResponseEntity.ok(service.resolver(id));
    }
}
