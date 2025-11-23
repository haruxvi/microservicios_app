package com.microservice.auth_service.microservice_auth_service.service;

import com.microservice.auth_service.microservice_auth_service.dto.UsuarioRequest;
import com.microservice.auth_service.microservice_auth_service.dto.UsuarioResponse;
import com.microservice.auth_service.microservice_auth_service.model.Estado;
import com.microservice.auth_service.microservice_auth_service.model.Rol;
import com.microservice.auth_service.microservice_auth_service.model.Usuario;
import com.microservice.auth_service.microservice_auth_service.repository.EstadoRepository;
import com.microservice.auth_service.microservice_auth_service.repository.RolRepository;
import com.microservice.auth_service.microservice_auth_service.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final EstadoRepository estadoRepository;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          EstadoRepository estadoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.estadoRepository = estadoRepository;
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

        Usuario usuario = new Usuario(
                request.getNombre(),
                request.getCorreo(),
                request.getClave(),
                null, // fotoPerfil por ahora null
                rol,
                estado,
                request.getPuntaje() != null ? request.getPuntaje() : 0,
                request.getPuntajeGlobal() != null ? request.getPuntajeGlobal() : 0
        );

        Usuario saved = usuarioRepository.save(usuario);
        return toResponse(saved);
    }

    public UsuarioResponse update(Long id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + id));

        if (!usuario.getCorreo().equalsIgnoreCase(request.getCorreo())
                && usuarioRepository.existsByCorreoIgnoreCase(request.getCorreo())) {
            throw new RuntimeException("Ya existe un usuario con ese correo");
        }

        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        Estado estado = estadoRepository.findById(request.getIdEstado())
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        usuario.setNombre(request.getNombre());
        usuario.setCorreo(request.getCorreo());
        usuario.setClave(request.getClave());
        usuario.setRol(rol);
        usuario.setEstado(estado);
        usuario.setPuntaje(request.getPuntaje());
        usuario.setPuntajeGlobal(request.getPuntajeGlobal());

        Usuario updated = usuarioRepository.save(usuario);
        return toResponse(updated);
    }

    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRol() != null ? usuario.getRol().getNombre() : null,
                usuario.getEstado() != null ? usuario.getEstado().getNombre() : null,
                usuario.getPuntaje(),
                usuario.getPuntajeGlobal()
        );
    }
}
