package com.microservice.feedbak_service.service;

import com.microservice.feedbak_service.dto.FeedbackRequest;
import com.microservice.feedbak_service.dto.FeedbackResponse;
import com.microservice.feedbak_service.model.Feedback;
import com.microservice.feedbak_service.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackService {

    private final FeedbackRepository repository;

    public FeedbackResponse crear(FeedbackRequest req) {
        Feedback f = new Feedback();
        f.setUsuarioId(req.usuarioId());
        f.setMensaje(req.mensaje());
        f.setTipo(req.tipo().toUpperCase());
        f.setDestino(req.destino().toUpperCase());
        f.setFecha(LocalDateTime.now());
        f.setResuelto(false);

        Feedback guardado = repository.save(f);
        return toResponse(guardado);
    }

    public List<FeedbackResponse> todos() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<FeedbackResponse> pendientes() {
        return repository.findByResueltoFalseOrderByFechaDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<FeedbackResponse> porUsuario(Long usuarioId) {
        return repository.findByUsuarioIdOrderByFechaDesc(usuarioId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public FeedbackResponse resolver(Long id) {
        Feedback f = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback no encontrado"));
        f.setResuelto(true);
        Feedback actualizado = repository.save(f);
        return toResponse(actualizado);
    }

    private FeedbackResponse toResponse(Feedback f) {
        return new FeedbackResponse(
                f.getIdFeedback(),
                f.getMensaje(),
                f.getTipo(),
                f.getDestino(),
                f.getFecha(),
                f.isResuelto(),
                f.getUsuarioId()
        );
    }
}
