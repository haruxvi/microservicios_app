package com.microservice.auth_service.microservice_auth_service;

import com.microservice.auth_service.microservice_auth_service.dto.UsuarioRequest;
import com.microservice.auth_service.microservice_auth_service.dto.UsuarioResponse;
import com.microservice.auth_service.microservice_auth_service.model.Estado;
import com.microservice.auth_service.microservice_auth_service.model.Rol;
import com.microservice.auth_service.microservice_auth_service.model.Usuario;
import com.microservice.auth_service.microservice_auth_service.repository.EstadoRepository;
import com.microservice.auth_service.microservice_auth_service.repository.RolRepository;
import com.microservice.auth_service.microservice_auth_service.repository.UsuarioRepository;
import com.microservice.auth_service.microservice_auth_service.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private EstadoRepository estadoRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Rol rol;
    private Estado estado;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        rol = new Rol("ADMIN");
        rol.setId(1L);

        estado = new Estado("ACTIVO");
        estado.setId(1L);

        usuario = new Usuario();
        usuario.setId(5L);
        usuario.setNombre("Juan");
        usuario.setCorreo("juan@example.com");
        usuario.setClave("1234");
        usuario.setRol(rol);
        usuario.setEstado(estado);
        usuario.setPuntaje(10);
        usuario.setPuntajeGlobal(100);
    }

    @Test
    void create_debeCrearUsuarioCorrectamente() {
        UsuarioRequest request = new UsuarioRequest();
        request.setNombre("Juan");
        request.setCorreo("juan@example.com");
        request.setClave("1234");
        request.setIdRol(1L);
        request.setIdEstado(1L);
        request.setPuntaje(10);
        request.setPuntajeGlobal(100);

        when(usuarioRepository.existsByCorreoIgnoreCase("juan@example.com")).thenReturn(false);
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(5L);
            return u;
        });

        UsuarioResponse response = usuarioService.create(request);

        assertNotNull(response);
        assertEquals(5L, response.getId());
        assertEquals("Juan", response.getNombre());
        assertEquals("juan@example.com", response.getCorreo());
        assertEquals("ADMIN", response.getRol());
        assertEquals("ACTIVO", response.getEstado());
        assertEquals(10, response.getPuntaje());
        assertEquals(100, response.getPuntajeGlobal());

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        Usuario guardado = captor.getValue();
        assertEquals("Juan", guardado.getNombre());
        assertEquals("juan@example.com", guardado.getCorreo());
        assertEquals("1234", guardado.getClave());
        assertEquals(rol, guardado.getRol());
        assertEquals(estado, guardado.getEstado());
    }

    @Test
    void create_debeLanzarExcepcionSiCorreoExiste() {
        UsuarioRequest request = new UsuarioRequest();
        request.setNombre("Juan");
        request.setCorreo("juan@example.com");

        when(usuarioRepository.existsByCorreoIgnoreCase("juan@example.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> usuarioService.create(request));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void getAll_debeRetornarListadoDeUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioResponse> lista = usuarioService.getAll();

        assertEquals(1, lista.size());
        UsuarioResponse resp = lista.get(0);
        assertEquals(usuario.getId(), resp.getId());
        assertEquals(usuario.getCorreo(), resp.getCorreo());
    }

    @Test
    void getById_debeRetornarUsuario() {
        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario));

        UsuarioResponse resp = usuarioService.getById(5L);

        assertEquals(5L, resp.getId());
        assertEquals("Juan", resp.getNombre());
    }

    @Test
    void update_debeActualizarUsuario() {
        UsuarioRequest request = new UsuarioRequest();
        request.setNombre("Juan Actualizado");
        request.setCorreo("juan2@example.com");
        request.setClave("abcd");
        request.setIdRol(1L);
        request.setIdEstado(1L);
        request.setPuntaje(20);
        request.setPuntajeGlobal(200);

        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByCorreoIgnoreCase("juan2@example.com")).thenReturn(false);
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        UsuarioResponse resp = usuarioService.update(5L, request);

        assertEquals("Juan Actualizado", resp.getNombre());
        assertEquals("juan2@example.com", resp.getCorreo());
        assertEquals(20, resp.getPuntaje());
        assertEquals(200, resp.getPuntajeGlobal());
    }

    @Test
    void delete_debeEliminarUsuario() {
        when(usuarioRepository.existsById(5L)).thenReturn(true);

        usuarioService.delete(5L);

        verify(usuarioRepository).deleteById(5L);
    }
}
