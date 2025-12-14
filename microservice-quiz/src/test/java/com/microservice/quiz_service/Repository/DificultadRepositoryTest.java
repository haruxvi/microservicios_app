package com.microservice.quiz_service.Repository;

import com.microservice.quiz_service.model.Dificultad;
import com.microservice.quiz_service.repository.DificultadRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class DificultadRepositoryTest {

    @Autowired
    private DificultadRepository dificultadRepository;

    @Test
    void debeGuardarYListarDificultades() {
        Dificultad d = new Dificultad("Fácil");
        dificultadRepository.save(d);

        assertThat(dificultadRepository.findAll()).isNotEmpty();
    }
}
