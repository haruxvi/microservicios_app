package com.microservice.auth_service.microservice_auth_service.repository;

import com.microservice.auth_service.microservice_auth_service.model.Estado;
import com.microservice.auth_service.microservice_auth_service.model.Rol;
import com.microservice.auth_service.microservice_auth_service.model.Usuario;
import com.microservice.auth_service.microservice_auth_service.repository.EstadoRepository;
import com.microservice.auth_service.microservice_auth_service.repository.RolRepository;
import com.microservice.auth_service.microservice_auth_service.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Test
    void debeGuardarYEncontrarPorCorreo() {
        Rol rol = new Rol("ADMIN");
        rol = rolRepository.save(rol);

        Estado estado = new Estado("ACTIVO");
        estado = estadoRepository.save(estado);

        Usuario u = new Usuario();
        u.setNombre("Juan");
        u.setCorreo("juan@example.com");
        u.setClave("1234");
        u.setRol(rol);
        u.setEstado(estado);
        u.setPuntaje(10);
        u.setPuntajeGlobal(100);

        usuarioRepository.save(u);

        Optional<Usuario> encontrado = usuarioRepository.findByCorreoIgnoreCase("JUAN@example.com");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNombre()).isEqualTo("Juan");
        assertThat(usuarioRepository.existsByCorreoIgnoreCase("juan@example.com")).isTrue();
    }

    @Test
    void debeEncontrarPorNombreIgnoreCase() {
        Rol rol = rolRepository.save(new Rol("ADMIN"));
        Estado estado = estadoRepository.save(new Estado("ACTIVO"));

        Usuario u = new Usuario();
        u.setNombre("Camila");
        u.setCorreo("camila@example.com");
        u.setClave("1234");
        u.setRol(rol);
        u.setEstado(estado);
        u.setPuntaje(0);
        u.setPuntajeGlobal(0);

        usuarioRepository.save(u);

        Optional<Usuario> encontrado = usuarioRepository.findByNombreIgnoreCase("cAmIlA");
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getCorreo()).isEqualTo("camila@example.com");
    }
}
