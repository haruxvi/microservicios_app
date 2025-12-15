package com.microservice.quiz_service.services;

import com.microservice.quiz_service.dto.CategoriaRequest;
import com.microservice.quiz_service.dto.CategoriaResponse;
import com.microservice.quiz_service.model.Categoria;
import com.microservice.quiz_service.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaResponse> getAll() {
        return categoriaRepository.findAll()
                .stream()
                .map(c -> new CategoriaResponse(c.getId(), c.getNombre()))
                .toList();
    }

    public CategoriaResponse create(CategoriaRequest request) {
        Categoria c = new Categoria(request.getNombre());
        Categoria saved = categoriaRepository.save(c);
        return new CategoriaResponse(saved.getId(), saved.getNombre());
    }
}
