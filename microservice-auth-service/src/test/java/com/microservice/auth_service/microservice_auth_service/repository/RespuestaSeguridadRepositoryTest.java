package com.microservice.auth_service.microservice_auth_service.repository;

import com.microservice.auth_service.microservice_auth_service.model.RespuestaSeguridad;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RespuestaSeguridadRepositoryTest {

    @Autowired
    private RespuestaSeguridadRepository repo;

    @Test
    void save_ok() {
        RespuestaSeguridad r = new RespuestaSeguridad();
        r.setUsuarioId(1L);
        r.setPreguntaId(1);
        r.setRespuestaHash("hash");

        RespuestaSeguridad saved = repo.save(r);

        assertNotNull(saved.getIdRespuesta());
        assertEquals(1L, saved.getUsuarioId());
        assertEquals(1, saved.getPreguntaId());
    }
}
