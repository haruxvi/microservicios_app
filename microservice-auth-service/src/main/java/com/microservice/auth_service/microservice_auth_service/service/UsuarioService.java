package com.microservice.auth_service.microservice_auth_service.service;

import com.microservice.auth_service.microservice_auth_service.dto.ActualizarFotoPerfilRequest;
import com.microservice.auth_service.microservice_auth_service.dto.UsuarioRequest;
import com.microservice.auth_service.microservice_auth_service.dto.UsuarioResponse;
import com.microservice.auth_service.microservice_auth_service.model.Estado;
import com.microservice.auth_service.microservice_auth_service.model.Rol;
import com.microservice.auth_service.microservice_auth_service.model.Usuario;
import com.microservice.auth_service.microservice_auth_service.repository.EstadoRepository;
import com.microservice.auth_service.microservice_auth_service.repository.RolRepository;
import com.microservice.auth_service.microservice_auth_service.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final EstadoRepository estadoRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          EstadoRepository estadoRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.estadoRepository = estadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioResponse> getAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UsuarioResponse getById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + id));
        return toResponse(usuario);
    }

    public UsuarioResponse create(UsuarioRequest request) {
        if (usuarioRepository.existsByCorreoIgnoreCase(request.getCorreo())) {
            throw new RuntimeException("Ya existe un usuario con ese correo");
        }

        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        Estado estado = estadoRepository.findById(request.getIdEstado())
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

      
        String hashedPassword = passwordEncoder.encode(request.getClave());

        byte[] fotoBytes = null;
        if (request.getFotoPerfilBase64() != null && !request.getFotoPerfilBase64().isBlank()) {
            fotoBytes = Base64.getDecoder().decode(request.getFotoPerfilBase64());
        }

        Usuario usuario = new Usuario(
        request.getNombre(),
        request.getCorreo(),
        hashedPassword,
        fotoBytes,
        rol,
        estado
            );

            // Puntajes opcionales
            usuario.setPuntaje(
                    request.getPuntaje() != null ? request.getPuntaje() : 0
            );
            usuario.setPuntajeGlobal(
                    request.getPuntajeGlobal() != null ? request.getPuntajeGlobal() : 0
            );


        Usuario saved = usuarioRepository.save(usuario);
        return toResponse(saved);
    }

    public UsuarioResponse update(Long id, UsuarioRequest request) {
        System.out.println("UPDATE USUARIO id=" + id);

        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + id));

            // Validación de correo duplicado
            if (!usuario.getCorreo().equalsIgnoreCase(request.getCorreo())
                    && usuarioRepository.existsByCorreoIgnoreCase(request.getCorreo())) {
                throw new RuntimeException("Ya existe un usuario con ese correo");
            }

            Rol rol = rolRepository.findById(request.getIdRol())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con id " + request.getIdRol()));

            Estado estado = estadoRepository.findById(request.getIdEstado())
                    .orElseThrow(() -> new RuntimeException("Estado no encontrado con id " + request.getIdEstado()));

            usuario.setNombre(request.getNombre());
            usuario.setCorreo(request.getCorreo());

            if (request.getClave() != null && !request.getClave().isBlank()) {
                String hashed = passwordEncoder.encode(request.getClave());
                usuario.setClave(hashed);
            }

            usuario.setRol(rol);
            usuario.setEstado(estado);

            usuario.setPuntaje(request.getPuntaje() != null ? request.getPuntaje() : 0);
            usuario.setPuntajeGlobal(request.getPuntajeGlobal() != null ? request.getPuntajeGlobal() : 0);

            // ---------- FOTO PERFIL ----------
            System.out.println("fotoPerfilBase64 null? " + (request.getFotoPerfilBase64() == null));
            System.out.println("fotoPerfilBase64 length = " +
                    (request.getFotoPerfilBase64() != null ? request.getFotoPerfilBase64().length() : 0));

            if (request.getFotoPerfilBase64() != null) {
                if (request.getFotoPerfilBase64().isBlank()) {
                    // Si mandas "", se borra la foto
                    usuario.setFotoPerfil(null);
                } else {
                    try {
                        byte[] bytes = java.util.Base64.getDecoder().decode(request.getFotoPerfilBase64());
                        System.out.println("bytes length = " + bytes.length);
                        usuario.setFotoPerfil(bytes);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Base64 inválido en fotoPerfilBase64", e);
                    }
                }
            }

            Usuario updated = usuarioRepository.save(usuario);
            System.out.println("Usuario actualizado OK con foto para id=" + id);

            return toResponse(updated);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        String fotoBase64 = null;
        if (usuario.getFotoPerfil() != null && usuario.getFotoPerfil().length > 0) {
            fotoBase64 = Base64.getEncoder().encodeToString(usuario.getFotoPerfil());
        }

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRol() != null ? usuario.getRol().getNombre() : null,
                usuario.getEstado() != null ? usuario.getEstado().getNombre() : null,
                usuario.getPuntaje(),
                usuario.getPuntajeGlobal(),
                fotoBase64
        );
    }

    @Transactional(readOnly = true)
    public int obtenerPuntajeGlobal(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Integer actual = usuario.getPuntajeGlobal();
        return actual != null ? actual : 0;
    }

    @Transactional
    public int actualizarPuntajeGlobal(Long usuarioId, int delta) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        int actual = usuario.getPuntajeGlobal() != null ? usuario.getPuntajeGlobal() : 0;
        int nuevo = actual + delta;
        usuario.setPuntajeGlobal(nuevo);

        usuarioRepository.save(usuario);
        return nuevo;
    }

    public void actualizarFotoPerfil(Long idUsuario, ActualizarFotoPerfilRequest request) {
        System.out.println("actualizarFotoPerfil -> idUsuario=" + idUsuario);

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + idUsuario));

        System.out.println("fotoBase64 null? " + (request.fotoBase64() == null));
        System.out.println("fotoBase64 length = " +
                (request.fotoBase64() != null ? request.fotoBase64().length() : 0));

        if (request.fotoBase64() != null && !request.fotoBase64().isBlank()) {
            try {
                byte[] bytes = java.util.Base64.getDecoder().decode(request.fotoBase64());
                System.out.println("bytes length = " + bytes.length);
                usuario.setFotoPerfil(bytes);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                throw new RuntimeException("Base64 inválido", e);
            }
        } else {
            usuario.setFotoPerfil(null);
        }

        usuarioRepository.save(usuario);
        System.out.println("Foto de perfil actualizada OK para usuario " + idUsuario);
    }

    public UsuarioResponse loginPorIdentificadorYClave(String identificador, String clavePlano) {
    // Puedes elegir: buscar por correo o por nombre
    Optional<Usuario> opt = usuarioRepository.findByCorreoIgnoreCase(identificador);

    if (opt.isEmpty()) {
        // si quieres aceptar también nombre de usuario:
        opt = usuarioRepository.findByNombreIgnoreCase(identificador);
    }

    Usuario usuario = opt.orElseThrow(
            () -> new RuntimeException("Credenciales inválidas")
    );

    // Validar con BCrypt
    if (!passwordEncoder.matches(clavePlano, usuario.getClave())) {
        throw new RuntimeException("Credenciales inválidas");
    }

    return toResponse(usuario);
}

@Transactional
public void actualizarPasswordPorCorreo(String identificador, String nuevaClave) {
    // Aquí puedes buscar por correo o por nombre, según como lo uses en Android
    Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(identificador)
            .orElseThrow(() -> new RuntimeException("No se encontró un usuario con ese correo"));

    String hashed = passwordEncoder.encode(nuevaClave);
    usuario.setClave(hashed);

    usuarioRepository.save(usuario);
}


}
