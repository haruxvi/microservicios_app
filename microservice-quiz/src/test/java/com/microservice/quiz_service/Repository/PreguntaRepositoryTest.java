package com.microservice.quiz_service.Repository;

import com.microservice.quiz_service.model.Categoria;
import com.microservice.quiz_service.model.Dificultad;
import com.microservice.quiz_service.model.Estado;
import com.microservice.quiz_service.model.Pregunta;
import com.microservice.quiz_service.repository.CategoriaRepository;
import com.microservice.quiz_service.repository.DificultadRepository;
import com.microservice.quiz_service.repository.EstadoRepository;
import com.microservice.quiz_service.repository.PreguntaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class PreguntaRepositoryTest {

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private DificultadRepository dificultadRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Test
    void debeGuardarYFiltrarPorCategoriaYDificultad() {
        Categoria categoria = categoriaRepository.save(new Categoria("Matemáticas"));
        Dificultad dificultad = dificultadRepository.save(new Dificultad("Fácil"));
        Estado estado = estadoRepository.save(new Estado("ACTIVA"));

        Pregunta p = new Pregunta("¿2+2?", categoria, dificultad, estado);
        preguntaRepository.save(p);

        List<Pregunta> porCategoria = preguntaRepository.findByCategoria_Id(categoria.getId());
        List<Pregunta> porDificultad = preguntaRepository.findByDificultad_Id(dificultad.getId());
        List<Pregunta> porCatDif = preguntaRepository
                .findByCategoria_IdAndDificultad_Id(categoria.getId(), dificultad.getId());

        assertThat(porCategoria).isNotEmpty();
        assertThat(porDificultad).isNotEmpty();
        assertThat(porCatDif).isNotEmpty();
    }
}
