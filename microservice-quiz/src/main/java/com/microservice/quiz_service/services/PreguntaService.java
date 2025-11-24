package com.microservice.quiz_service.services;

import com.microservice.quiz_service.dto.*;
import com.microservice.quiz_service.model.Categoria;
import com.microservice.quiz_service.model.Dificultad;
import com.microservice.quiz_service.model.Estado;
import com.microservice.quiz_service.model.Opcion;
import com.microservice.quiz_service.model.Pregunta;
import com.microservice.quiz_service.repository.CategoriaRepository;
import com.microservice.quiz_service.repository.DificultadRepository;
import com.microservice.quiz_service.repository.EstadoRepository;
import com.microservice.quiz_service.repository.PreguntaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PreguntaService {

    private final PreguntaRepository preguntaRepository;
    private final CategoriaRepository categoriaRepository;
    private final DificultadRepository dificultadRepository;
    private final EstadoRepository estadoRepository;

    public PreguntaService(PreguntaRepository preguntaRepository,
                           CategoriaRepository categoriaRepository,
                           DificultadRepository dificultadRepository,
                           EstadoRepository estadoRepository) {
        this.preguntaRepository = preguntaRepository;
        this.categoriaRepository = categoriaRepository;
        this.dificultadRepository = dificultadRepository;
        this.estadoRepository = estadoRepository;
    }

    public List<PreguntaResponse> getAll() {
        return preguntaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PreguntaResponse getById(Long id) {
        Pregunta p = preguntaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada"));
        return toResponse(p);
    }

    public List<PreguntaResponse> getByCategoria(Long idCategoria) {
        return preguntaRepository.findByCategoria_Id(idCategoria)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<PreguntaResponse> getByDificultad(Long idDificultad) {
        return preguntaRepository.findByDificultad_Id(idDificultad)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<PreguntaResponse> getByCategoriaAndDificultad(Long idCategoria, Long idDificultad) {
        return preguntaRepository.findByCategoria_IdAndDificultad_Id(idCategoria, idDificultad)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PreguntaResponse create(PreguntaRequest request) {
        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        Dificultad dificultad = dificultadRepository.findById(request.getIdDificultad())
                .orElseThrow(() -> new RuntimeException("Dificultad no encontrada"));
        Estado estado = estadoRepository.findById(request.getIdEstado())
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        Pregunta p = new Pregunta(request.getEnunciado(), categoria, dificultad, estado);

        if (request.getOpciones() == null || request.getOpciones().isEmpty()) {
            throw new RuntimeException("La pregunta debe tener al menos una opción");
        }

        request.getOpciones().forEach(oReq -> {
            Opcion o = new Opcion(oReq.getTexto(), oReq.isEsCorrecta());
            p.addOpcion(o);
        });

        Pregunta saved = preguntaRepository.save(p);
        return toResponse(saved);
    }

    public PreguntaResponse update(Long id, PreguntaRequest request) {
        Pregunta p = preguntaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada"));

        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        Dificultad dificultad = dificultadRepository.findById(request.getIdDificultad())
                .orElseThrow(() -> new RuntimeException("Dificultad no encontrada"));
        Estado estado = estadoRepository.findById(request.getIdEstado())
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        // Actualizar datos básicos
        p.setEnunciado(request.getEnunciado());
        p.setCategoria(categoria);
        p.setDificultad(dificultad);
        p.setEstado(estado);

        // Limpiar opciones anteriores y volver a crearlas
        p.getOpciones().clear();

        if (request.getOpciones() == null || request.getOpciones().isEmpty()) {
            throw new RuntimeException("La pregunta debe tener al menos una opción");
        }

        request.getOpciones().forEach(oReq -> {
            Opcion o = new Opcion(oReq.getTexto(), oReq.isEsCorrecta());
            p.addOpcion(o);
        });

        Pregunta saved = preguntaRepository.save(p);
        return toResponse(saved);
    }


    public void delete(Long id) {
        if (!preguntaRepository.existsById(id)) {
            throw new RuntimeException("Pregunta no encontrada");
        }
        preguntaRepository.deleteById(id);
    }

    private PreguntaResponse toResponse(Pregunta p) {
        List<OpcionResponse> opciones = p.getOpciones()
                .stream()
                .map(o -> new OpcionResponse(o.getId(), o.getTexto(), o.isEsCorrecta()))
                .toList();

        return new PreguntaResponse(
                p.getId(),
                p.getEnunciado(),
                p.getCategoria() != null ? p.getCategoria().getNombre() : null,
                p.getDificultad() != null ? p.getDificultad().getNombre() : null,
                p.getEstado() != null ? p.getEstado().getNombre() : null,
                opciones
        );
    }
}
