package com.microservice.auth_service.microservice_auth_service.service;

import com.microservice.auth_service.microservice_auth_service.dto.ActualizarFotoPerfilRequest;
import com.microservice.auth_service.microservice_auth_service.dto.UsuarioRequest;
import com.microservice.auth_service.microservice_auth_service.model.Estado;
import com.microservice.auth_service.microservice_auth_service.model.Rol;
import com.microservice.auth_service.microservice_auth_service.model.Usuario;
import com.microservice.auth_service.microservice_auth_service.repository.EstadoRepository;
import com.microservice.auth_service.microservice_auth_service.repository.RolRepository;
import com.microservice.auth_service.microservice_auth_service.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceUnitTest {

    @Mock UsuarioRepository usuarioRepository;
    @Mock RolRepository rolRepository;
    @Mock EstadoRepository estadoRepository;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks UsuarioService service;

    @Test
    void create_correoDuplicado_lanzaRuntime() {
        UsuarioRequest req = baseUsuarioRequest();
        req.setCorreo("dup@a.com");

        when(usuarioRepository.existsByCorreoIgnoreCase("dup@a.com")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.create(req));
        assertTrue(ex.getMessage().toLowerCase().contains("ya existe"));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void create_ok_guardaUsuarioConClaveHasheada_yFotoDecodificada() {
        UsuarioRequest req = baseUsuarioRequest();
        req.setCorreo("a@a.com");
        req.setClave("plain");
        req.setIdRol(2L);
        req.setIdEstado(1L);

        byte[] foto = "img".getBytes();
        req.setFotoPerfilBase64(Base64.getEncoder().encodeToString(foto));

        when(usuarioRepository.existsByCorreoIgnoreCase("a@a.com")).thenReturn(false);
        when(rolRepository.findById(2L)).thenReturn(Optional.of(new Rol("USER")));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(new Estado("ACTIVO")));
        when(passwordEncoder.encode("plain")).thenReturn("HASH");

        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(10L);
            return u;
        });

        var res = service.create(req);

        assertEquals(10L, res.getId());
        assertEquals("a@a.com", res.getCorreo());
        assertEquals("USER", res.getRol());
        assertEquals("ACTIVO", res.getEstado());
        assertNotNull(res.getFotoPerfilBase64());

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertEquals("HASH", captor.getValue().getClave());
        assertArrayEquals(foto, captor.getValue().getFotoPerfil());
    }

    @Test
    void delete_siNoExiste_lanzaRuntime() {
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.delete(99L));
        assertTrue(ex.getMessage().toLowerCase().contains("no encontrado"));
        verify(usuarioRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_ok_elimina() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void actualizarFotoPerfil_base64Invalido_lanzaRuntime() {
        Usuario u = new Usuario();
        u.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.actualizarFotoPerfil(1L, new ActualizarFotoPerfilRequest("%%%NOBASE64%%%")));

        assertTrue(ex.getMessage().toLowerCase().contains("base64"));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void actualizarFotoPerfil_ok_guardaBytes() {
        Usuario u = new Usuario();
        u.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));

        byte[] bytes = "foto".getBytes();
        String base64 = Base64.getEncoder().encodeToString(bytes);

        service.actualizarFotoPerfil(1L, new ActualizarFotoPerfilRequest(base64));

        assertArrayEquals(bytes, u.getFotoPerfil());
        verify(usuarioRepository).save(u);
    }

    @Test
    void login_credencialesInvalidas_usuarioNoExiste() {
        when(usuarioRepository.findByCorreoIgnoreCase("x")).thenReturn(Optional.empty());
        when(usuarioRepository.findByNombreIgnoreCase("x")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.loginPorIdentificadorYClave("x", "pw"));
        assertTrue(ex.getMessage().toLowerCase().contains("credenciales"));
    }

    @Test
    void login_credencialesInvalidas_passwordNoMatch() {
        Usuario u = new Usuario();
        u.setId(1L);
        u.setNombre("N");
        u.setCorreo("c@c.com");
        u.setClave("HASH");

        when(usuarioRepository.findByCorreoIgnoreCase("c@c.com")).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("pw", "HASH")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.loginPorIdentificadorYClave("c@c.com", "pw"));
        assertTrue(ex.getMessage().toLowerCase().contains("credenciales"));
    }

    @Test
    void login_ok_devuelveUsuarioResponse() {
        Usuario u = new Usuario();
        u.setId(1L);
        u.setNombre("N");
        u.setCorreo("c@c.com");
        u.setClave("HASH");
        u.setRol(new Rol("USER"));
        u.setEstado(new Estado("ACTIVO"));

        when(usuarioRepository.findByCorreoIgnoreCase("c@c.com")).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("pw", "HASH")).thenReturn(true);

        var res = service.loginPorIdentificadorYClave("c@c.com", "pw");

        assertEquals(1L, res.getId());
        assertEquals("c@c.com", res.getCorreo());
        assertEquals("USER", res.getRol());
        assertEquals("ACTIVO", res.getEstado());
    }

    @Test
    void actualizarPasswordPorCorreo_usuarioNoExiste_lanzaRuntime() {
        when(usuarioRepository.findByCorreoIgnoreCase("a@a.com")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.actualizarPasswordPorCorreo("a@a.com", "NuevaClave#2025"));
        assertTrue(ex.getMessage().toLowerCase().contains("no se encontró") || ex.getMessage().toLowerCase().contains("no se encontro"));
    }

    @Test
    void actualizarPasswordPorCorreo_ok_hasheaYGuarda() {
        Usuario u = new Usuario();
        u.setId(1L);

        when(usuarioRepository.findByCorreoIgnoreCase("a@a.com")).thenReturn(Optional.of(u));
        when(passwordEncoder.encode("NuevaClave#2025")).thenReturn("HASHED");

        service.actualizarPasswordPorCorreo("a@a.com", "NuevaClave#2025");

        assertEquals("HASHED", u.getClave());
        verify(usuarioRepository).save(u);
    }

    private static UsuarioRequest baseUsuarioRequest() {
        UsuarioRequest req = new UsuarioRequest();
        req.setNombre("Nombre");
        req.setCorreo("base@a.com");
        req.setClave("Plain#123");
        req.setIdRol(1L);
        req.setIdEstado(1L);
        req.setPuntaje(0);
        req.setPuntajeGlobal(0);
        return req;
    }
}
