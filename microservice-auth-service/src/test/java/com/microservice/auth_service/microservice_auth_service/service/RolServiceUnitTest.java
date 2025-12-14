package com.microservice.auth_service.microservice_auth_service.service;

import com.microservice.auth_service.microservice_auth_service.dto.RolRequest;
import com.microservice.auth_service.microservice_auth_service.model.Rol;
import com.microservice.auth_service.microservice_auth_service.repository.RolRepository;
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
class RolServiceUnitTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolService rolService;

    @Test
    void getAll_devuelveListaMapeada() {
        Rol r1 = new Rol("ADMIN");
        r1.setId(1L);
        Rol r2 = new Rol("USER");
        r2.setId(2L);

        when(rolRepository.findAll()).thenReturn(List.of(r1, r2));

        var res = rolService.getAll();

        assertEquals(2, res.size());
        assertEquals(1L, res.get(0).getId());
        assertEquals("ADMIN", res.get(0).getNombre());
        assertEquals(2L, res.get(1).getId());
        assertEquals("USER", res.get(1).getNombre());
    }

    @Test
    void create_guardaYDevuelveResponse() {
        RolRequest req = new RolRequest("MOD");

        Rol saved = new Rol("MOD");
        saved.setId(7L);

        when(rolRepository.save(any(Rol.class))).thenReturn(saved);

        var res = rolService.create(req);

        assertEquals(7L, res.getId());
        assertEquals("MOD", res.getNombre());

        ArgumentCaptor<Rol> captor = ArgumentCaptor.forClass(Rol.class);
        verify(rolRepository).save(captor.capture());
        assertEquals("MOD", captor.getValue().getNombre());
    }
}
