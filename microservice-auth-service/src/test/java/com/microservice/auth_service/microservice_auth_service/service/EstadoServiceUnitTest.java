package com.microservice.auth_service.microservice_auth_service.service;

import com.microservice.auth_service.microservice_auth_service.dto.EstadoRequest;
import com.microservice.auth_service.microservice_auth_service.model.Estado;
import com.microservice.auth_service.microservice_auth_service.repository.EstadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadoServiceUnitTest {

    @Mock
    private EstadoRepository estadoRepository;

    @InjectMocks
    private EstadoService estadoService;

    @Test
    void getAll_devuelveListaMapeada() {
        Estado e1 = new Estado("ACTIVO");
        e1.setId(1L);
        Estado e2 = new Estado("INACTIVO");
        e2.setId(2L);

        when(estadoRepository.findAll()).thenReturn(List.of(e1, e2));

        var res = estadoService.getAll();

        assertEquals(2, res.size());
        assertEquals(1L, res.get(0).getId());
        assertEquals("ACTIVO", res.get(0).getNombre());
        assertEquals(2L, res.get(1).getId());
        assertEquals("INACTIVO", res.get(1).getNombre());
    }

    @Test
    void create_guardaYDevuelveResponse() {
        EstadoRequest req = new EstadoRequest("SUSPENDIDO");

        Estado saved = new Estado("SUSPENDIDO");
        saved.setId(10L);

        when(estadoRepository.save(any(Estado.class))).thenReturn(saved);

        var res = estadoService.create(req);

        assertEquals(10L, res.getId());
        assertEquals("SUSPENDIDO", res.getNombre());

        ArgumentCaptor<Estado> captor = ArgumentCaptor.forClass(Estado.class);
        verify(estadoRepository).save(captor.capture());
        assertEquals("SUSPENDIDO", captor.getValue().getNombre());
    }
}
