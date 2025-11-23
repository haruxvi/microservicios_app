package com.microservice.auth_service.microservice_auth_service.service;

import com.microservice.auth_service.microservice_auth_service.dto.EstadoRequest;
import com.microservice.auth_service.microservice_auth_service.dto.EstadoResponse;
import com.microservice.auth_service.microservice_auth_service.model.Estado;
import com.microservice.auth_service.microservice_auth_service.repository.EstadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EstadoService {

    private final EstadoRepository estadoRepository;

    public EstadoService(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }

    public List<EstadoResponse> getAll() {
        return estadoRepository.findAll()
                .stream()
                .map(e -> new EstadoResponse(e.getId(), e.getNombre()))
                .collect(Collectors.toList());
    }

    public EstadoResponse create(EstadoRequest request) {
        Estado estado = new Estado(request.getNombre());
        Estado saved = estadoRepository.save(estado);
        return new EstadoResponse(saved.getId(), saved.getNombre());
    }
}
