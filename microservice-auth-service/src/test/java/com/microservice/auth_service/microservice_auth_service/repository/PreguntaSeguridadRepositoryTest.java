package com.microservice.auth_service.microservice_auth_service.repository;

import com.microservice.auth_service.microservice_auth_service.model.PreguntaSeguridad;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PreguntaSeguridadRepositoryTest {

    @Autowired
    private PreguntaSeguridadRepository repo;

    @Test
    void save_and_findById_ok() {
        PreguntaSeguridad p = new PreguntaSeguridad();
        p.setId(1); // ✅ obligatorio si NO hay @GeneratedValue
        p.setTexto("¿Cuál es tu color favorito?");
        p.setActiva(true);

        PreguntaSeguridad saved = repo.save(p);

        assertNotNull(saved.getId());

        var found = repo.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("¿Cuál es tu color favorito?", found.get().getTexto());
    }
}

