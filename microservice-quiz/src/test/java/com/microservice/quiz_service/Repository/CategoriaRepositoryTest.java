package com.microservice.quiz_service.Repository;

import com.microservice.quiz_service.model.Categoria;
import com.microservice.quiz_service.repository.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void debeGuardarYListarCategorias() {
        Categoria c = new Categoria("Matemáticas");
        categoriaRepository.save(c);

        assertThat(categoriaRepository.findAll()).isNotEmpty();
    }
}
