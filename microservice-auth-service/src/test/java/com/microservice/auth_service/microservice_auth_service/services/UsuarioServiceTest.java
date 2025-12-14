package com.microservice.auth_service.microservice_auth_service.services;

import com.microservice.auth_service.microservice_auth_service.dto.ActualizarFotoPerfilRequest;
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

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // ✅ evita UnnecessaryStubbingException
class UsuarioServiceTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private RolRepository rolRepository;
    @Mock private EstadoRepository estadoRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private UsuarioService usuarioService;

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
        usuario.setClave("hashed");
        usuario.setRol(rol);
        usuario.setEstado(estado);
        usuario.setPuntaje(10);
        usuario.setPuntajeGlobal(100);

        // Stubs comunes (lenient para que no fallen si un test no los usa)
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
    }

    // ---------- CREATE ----------

    @Test
    void create_debeCrearUsuarioCorrectamente() {
        UsuarioRequest request = baseRequest();
        request.setFotoPerfilBase64(null);

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
    }

    @Test
    void create_debeCrearUsuarioConFotoBase64() {
        UsuarioRequest request = baseRequest();
        request.setFotoPerfilBase64(Base64.getEncoder().encodeToString("img".getBytes()));

        when(usuarioRepository.existsByCorreoIgnoreCase("juan@example.com")).thenReturn(false);
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(5L);
            return u;
        });

        UsuarioResponse response = usuarioService.create(request);

        assertEquals(5L, response.getId());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void create_debeLanzarExcepcionSiCorreoExiste() {
        UsuarioRequest request = baseRequest();

        when(usuarioRepository.existsByCorreoIgnoreCase("juan@example.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> usuarioService.create(request));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void create_debeLanzarExcepcionSiRolNoExiste() {
        UsuarioRequest request = baseRequest();

        when(usuarioRepository.existsByCorreoIgnoreCase("juan@example.com")).thenReturn(false);
        when(rolRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.create(request));
    }

    @Test
    void create_debeLanzarExcepcionSiEstadoNoExiste() {
        UsuarioRequest request = baseRequest();

        when(usuarioRepository.existsByCorreoIgnoreCase("juan@example.com")).thenReturn(false);
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(estadoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.create(request));
    }

    // ---------- GET ----------

    @Test
    void getAll_debeRetornarListadoDeUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioResponse> lista = usuarioService.getAll();

        assertEquals(1, lista.size());
        assertEquals(5L, lista.get(0).getId());
    }

    @Test
    void getById_debeRetornarUsuario() {
        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario));

        UsuarioResponse resp = usuarioService.getById(5L);

        assertEquals(5L, resp.getId());
        assertEquals("Juan", resp.getNombre());
    }

    @Test
    void getById_debeLanzarExcepcionSiNoExiste() {
        when(usuarioRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.getById(5L));
    }

    // ---------- UPDATE ----------

    @Test
    void update_debeActualizarUsuario_ok() {
        UsuarioRequest request = baseRequest();
        request.setNombre("Juan Actualizado");
        request.setCorreo("juan2@example.com");
        request.setClave("abcd");
        request.setFotoPerfilBase64(Base64.getEncoder().encodeToString("img".getBytes()));
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
    void update_debeLanzarExcepcionSiUsuarioNoExiste() {
        UsuarioRequest request = baseRequest();

        when(usuarioRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.update(5L, request));
    }

    @Test
    void update_debeLanzarExcepcionSiCorreoDuplicado() {
        UsuarioRequest request = baseRequest();
        request.setCorreo("otro@mail.com");

        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByCorreoIgnoreCase("otro@mail.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> usuarioService.update(5L, request));
    }

    @Test
    void update_noDebeCambiarClave_siRequestClaveVacia() {
        UsuarioRequest request = baseRequest();
        request.setCorreo("juan2@example.com");
        request.setClave("   "); // blank

        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByCorreoIgnoreCase("juan2@example.com")).thenReturn(false);
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        String claveAntes = usuario.getClave();

        usuarioService.update(5L, request);

        assertEquals(claveAntes, usuario.getClave());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void update_debeBorrarFoto_siFotoBase64EsVacia() {
        UsuarioRequest request = baseRequest();
        request.setCorreo("juan2@example.com");
        request.setFotoPerfilBase64("");

        usuario.setFotoPerfil("img".getBytes());

        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByCorreoIgnoreCase("juan2@example.com")).thenReturn(false);
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        usuarioService.update(5L, request);

        assertNull(usuario.getFotoPerfil());
    }

    @Test
    void update_debeLanzarExcepcion_siFotoBase64Invalida() {
        UsuarioRequest request = baseRequest();
        request.setCorreo("juan2@example.com");
        request.setFotoPerfilBase64("NO_ES_BASE64");

        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByCorreoIgnoreCase("juan2@example.com")).thenReturn(false);
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));

        assertThrows(RuntimeException.class, () -> usuarioService.update(5L, request));
    }

    @Test
    void update_debeLanzarExcepcionSiRolNoExiste() {
        UsuarioRequest request = baseRequest();
        request.setCorreo("juan2@example.com");

        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByCorreoIgnoreCase("juan2@example.com")).thenReturn(false);
        when(rolRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.update(5L, request));
    }

    @Test
    void update_debeLanzarExcepcionSiEstadoNoExiste() {
        UsuarioRequest request = baseRequest();
        request.setCorreo("juan2@example.com");

        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByCorreoIgnoreCase("juan2@example.com")).thenReturn(false);
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(estadoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.update(5L, request));
    }

    // ---------- DELETE ----------

    @Test
    void delete_debeEliminarUsuario() {
        when(usuarioRepository.existsById(5L)).thenReturn(true);

        usuarioService.delete(5L);

        verify(usuarioRepository).deleteById(5L);
    }

    @Test
    void delete_debeLanzarExcepcionSiNoExiste() {
        when(usuarioRepository.existsById(5L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> usuarioService.delete(5L));
    }

    // ---------- PUNTAJE GLOBAL ----------

    @Test
    void obtenerPuntajeGlobal_debeRetornarValorCuandoExiste() {
        usuario.setPuntajeGlobal(150);
        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario));

        int resultado = usuarioService.obtenerPuntajeGlobal(5L);

        assertEquals(150, resultado);
    }

    @Test
    void obtenerPuntajeGlobal_debeRetornarCeroCuandoEsNull() {
        usuario.setPuntajeGlobal(null);
        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario));

        int resultado = usuarioService.obtenerPuntajeGlobal(5L);

        assertEquals(0, resultado);
    }

    @Test
    void obtenerPuntajeGlobal_usuarioNoExiste() {
        when(usuarioRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> usuarioService.obtenerPuntajeGlobal(5L));
    }

    @Test
    void actualizarPuntajeGlobal_debeSumarDeltaYGuardar() {
        usuario.setPuntajeGlobal(80);
        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario));

        int nuevo = usuarioService.actualizarPuntajeGlobal(5L, 20);

        assertEquals(100, nuevo);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void actualizarPuntajeGlobal_debeUsarCeroSiPuntajeEsNull() {
        usuario.setPuntajeGlobal(null);
        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario));

        int nuevo = usuarioService.actualizarPuntajeGlobal(5L, 30);

        assertEquals(30, nuevo);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void actualizarPuntajeGlobal_usuarioNoExiste() {
        when(usuarioRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> usuarioService.actualizarPuntajeGlobal(5L, 10));
    }

    // ---------- FOTO PERFIL (PATCH) ----------

    @Test
    void actualizarFotoPerfil_conFotoValida_debeGuardarBytes() {
        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario));

        String base64 = Base64.getEncoder().encodeToString("img".getBytes());
        usuarioService.actualizarFotoPerfil(5L, new ActualizarFotoPerfilRequest(base64));

        assertNotNull(usuario.getFotoPerfil());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void actualizarFotoPerfil_conFotoNull_debeBorrar() {
        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario));

        usuario.setFotoPerfil("algo".getBytes());
        usuarioService.actualizarFotoPerfil(5L, new ActualizarFotoPerfilRequest(null));

        assertNull(usuario.getFotoPerfil());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void actualizarFotoPerfil_conBase64Invalido_debeLanzarExcepcion() {
        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario));

        assertThrows(RuntimeException.class, () ->
                usuarioService.actualizarFotoPerfil(5L, new ActualizarFotoPerfilRequest("NO_ES_BASE64"))
        );
    }

    @Test
    void actualizarFotoPerfil_usuarioNoExiste() {
        when(usuarioRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                usuarioService.actualizarFotoPerfil(5L, new ActualizarFotoPerfilRequest(null))
        );
    }

    // ---------- LOGIN ----------

    @Test
    void loginPorIdentificadorYClave_debeLogearPorCorreo() {
        when(usuarioRepository.findByCorreoIgnoreCase("juan@example.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(eq("1234"), anyString())).thenReturn(true);

        UsuarioResponse resp = usuarioService.loginPorIdentificadorYClave("juan@example.com", "1234");

        assertEquals("Juan", resp.getNombre());
    }

    @Test
    void loginPorIdentificadorYClave_debeLogearPorNombre_siNoExisteCorreo() {
        when(usuarioRepository.findByCorreoIgnoreCase("Juan")).thenReturn(Optional.empty());
        when(usuarioRepository.findByNombreIgnoreCase("Juan")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(eq("1234"), anyString())).thenReturn(true);

        UsuarioResponse resp = usuarioService.loginPorIdentificadorYClave("Juan", "1234");

        assertEquals("Juan", resp.getNombre());
    }

    @Test
    void loginPorIdentificadorYClave_usuarioNoExiste_debeFallar() {
        when(usuarioRepository.findByCorreoIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.findByNombreIgnoreCase(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> usuarioService.loginPorIdentificadorYClave("noexiste", "1234"));
    }

    @Test
    void loginPorIdentificadorYClave_passwordIncorrecta_debeFallar() {
        when(usuarioRepository.findByCorreoIgnoreCase(anyString())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> usuarioService.loginPorIdentificadorYClave("juan@example.com", "mal"));
    }

    // ---------- RESET PASSWORD ----------

    @Test
    void actualizarPasswordPorCorreo_ok() {
        when(usuarioRepository.findByCorreoIgnoreCase("juan@example.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("nueva")).thenReturn("hashedNueva");

        usuarioService.actualizarPasswordPorCorreo("juan@example.com", "nueva");

        assertEquals("hashedNueva", usuario.getClave());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void actualizarPasswordPorCorreo_noExisteUsuario() {
        when(usuarioRepository.findByCorreoIgnoreCase(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> usuarioService.actualizarPasswordPorCorreo("no@no.com", "nueva"));
    }

    // ---------- HELPERS ----------

    private UsuarioRequest baseRequest() {
        UsuarioRequest r = new UsuarioRequest();
        r.setNombre("Juan");
        r.setCorreo("juan@example.com");
        r.setClave("1234");
        r.setIdRol(1L);
        r.setIdEstado(1L);
        r.setPuntaje(10);
        r.setPuntajeGlobal(100);
        // por defecto, null para no disparar ramas de foto si no las necesitamos
        r.setFotoPerfilBase64(null);
        return r;
    }
}
