package com.microservice.auth_service.microservice_auth_service.service;

import com.microservice.auth_service.microservice_auth_service.dto.RolRequest;
import com.microservice.auth_service.microservice_auth_service.dto.RolResponse;
import com.microservice.auth_service.microservice_auth_service.model.Rol;
import com.microservice.auth_service.microservice_auth_service.repository.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public List<RolResponse> getAll() {
        return rolRepository.findAll()
                .stream()
                .map(r -> new RolResponse(r.getId(), r.getNombre()))
                .collect(Collectors.toList());
    }

    public RolResponse create(RolRequest request) {
        Rol rol = new Rol(request.getNombre());
        Rol saved = rolRepository.save(rol);
        return new RolResponse(saved.getId(), saved.getNombre());
    }
}
