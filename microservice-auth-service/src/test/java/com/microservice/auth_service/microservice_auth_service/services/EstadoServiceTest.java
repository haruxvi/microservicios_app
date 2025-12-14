package com.microservice.auth_service.microservice_auth_service.services;

import com.microservice.auth_service.microservice_auth_service.dto.EstadoRequest;
import com.microservice.auth_service.microservice_auth_service.dto.EstadoResponse;
import com.microservice.auth_service.microservice_auth_service.model.Estado;
import com.microservice.auth_service.microservice_auth_service.repository.EstadoRepository;
import com.microservice.auth_service.microservice_auth_service.service.EstadoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadoServiceTest {

    @Mock
    private EstadoRepository estadoRepository;

    @InjectMocks
    private EstadoService estadoService;

    @Test
    void getAll_debeRetornarListaDeEstados() {
        Estado e = new Estado("ACTIVO");
        e.setId(1L);

        when(estadoRepository.findAll()).thenReturn(List.of(e));

        List<EstadoResponse> lista = estadoService.getAll();

        assertEquals(1, lista.size());
        assertEquals("ACTIVO", lista.get(0).getNombre());
    }

    @Test
    void create_debeGuardarEstado() {
        EstadoRequest request = new EstadoRequest("SUSPENDIDO");
        Estado e = new Estado("SUSPENDIDO");
        e.setId(2L);

        when(estadoRepository.save(any(Estado.class))).thenReturn(e);

        EstadoResponse resp = estadoService.create(request);

        assertEquals(2L, resp.getId());
        assertEquals("SUSPENDIDO", resp.getNombre());
        verify(estadoRepository).save(any(Estado.class));
    }
}
