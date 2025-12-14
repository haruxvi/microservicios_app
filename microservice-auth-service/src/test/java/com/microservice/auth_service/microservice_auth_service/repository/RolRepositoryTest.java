package com.microservice.auth_service.microservice_auth_service.repository;

import com.microservice.auth_service.microservice_auth_service.model.Rol;
import com.microservice.auth_service.microservice_auth_service.repository.RolRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class RolRepositoryTest {

    @Autowired
    private RolRepository rolRepository;

    @Test
    void debeGuardarYListarRoles() {
        Rol r = new Rol("ADMIN");
        rolRepository.save(r);

        assertThat(rolRepository.findAll()).isNotEmpty();
    }
}
