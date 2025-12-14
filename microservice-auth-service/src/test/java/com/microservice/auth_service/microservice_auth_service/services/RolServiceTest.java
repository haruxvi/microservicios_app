package com.microservice.auth_service.microservice_auth_service.services;

import com.microservice.auth_service.microservice_auth_service.dto.RolRequest;
import com.microservice.auth_service.microservice_auth_service.dto.RolResponse;
import com.microservice.auth_service.microservice_auth_service.model.Rol;
import com.microservice.auth_service.microservice_auth_service.repository.RolRepository;
import com.microservice.auth_service.microservice_auth_service.service.RolService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolService rolService;

    @Test
    void getAll_debeRetornarListaDeRoles() {
        Rol rol = new Rol("ADMIN");
        rol.setId(1L);

        when(rolRepository.findAll()).thenReturn(List.of(rol));

        List<RolResponse> lista = rolService.getAll();

        assertEquals(1, lista.size());
        assertEquals("ADMIN", lista.get(0).getNombre());
    }

    @Test
    void create_debeGuardarRol() {
        RolRequest request = new RolRequest("USER");
        Rol rol = new Rol("USER");
        rol.setId(2L);

        when(rolRepository.save(any(Rol.class))).thenReturn(rol);

        RolResponse resp = rolService.create(request);

        assertEquals(2L, resp.getId());
        assertEquals("USER", resp.getNombre());
        verify(rolRepository).save(any(Rol.class));
    }
}
