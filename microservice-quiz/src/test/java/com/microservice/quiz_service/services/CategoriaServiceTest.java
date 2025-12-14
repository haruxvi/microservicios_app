package com.microservice.quiz_service.services;

import com.microservice.quiz_service.dto.CategoriaRequest;
import com.microservice.quiz_service.dto.CategoriaResponse;
import com.microservice.quiz_service.model.Categoria;
import com.microservice.quiz_service.repository.CategoriaRepository;
import com.microservice.quiz_service.services.CategoriaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void getAll_debeRetornarListaDeCategorias() {
        Categoria c = new Categoria("Matemáticas");
        c.setId(1L);

        when(categoriaRepository.findAll()).thenReturn(List.of(c));

        List<CategoriaResponse> lista = categoriaService.getAll();

        assertEquals(1, lista.size());
        assertEquals("Matemáticas", lista.get(0).getNombre());
    }

    @Test
    void create_debeGuardarCategoria() {
        CategoriaRequest request = new CategoriaRequest("Ciencia");
        Categoria saved = new Categoria("Ciencia");
        saved.setId(2L);

        when(categoriaRepository.save(any(Categoria.class))).thenReturn(saved);

        CategoriaResponse resp = categoriaService.create(request);

        assertEquals(2L, resp.getId());
        assertEquals("Ciencia", resp.getNombre());
        verify(categoriaRepository).save(any(Categoria.class));
    }
}
