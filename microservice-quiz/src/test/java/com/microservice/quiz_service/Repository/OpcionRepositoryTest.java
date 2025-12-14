package com.microservice.quiz_service.Repository;

import com.microservice.quiz_service.model.Categoria;
import com.microservice.quiz_service.model.Dificultad;
import com.microservice.quiz_service.model.Estado;
import com.microservice.quiz_service.model.Opcion;
import com.microservice.quiz_service.model.Pregunta;
import com.microservice.quiz_service.repository.CategoriaRepository;
import com.microservice.quiz_service.repository.DificultadRepository;
import com.microservice.quiz_service.repository.EstadoRepository;
import com.microservice.quiz_service.repository.OpcionRepository;
import com.microservice.quiz_service.repository.PreguntaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class OpcionRepositoryTest {

    @Autowired
    private OpcionRepository opcionRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private DificultadRepository dificultadRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Test
    void debeGuardarYListarOpciones() {
        // Crear y guardar dependencias requeridas
        Categoria categoria = new Categoria("Matemáticas");
        categoria = categoriaRepository.save(categoria);

        Dificultad dificultad = new Dificultad("Fácil");
        dificultad = dificultadRepository.save(dificultad);

        Estado estado = new Estado("ACTIVA");
        estado = estadoRepository.save(estado);

        Pregunta pregunta = new Pregunta("¿2+2?", categoria, dificultad, estado);
        pregunta = preguntaRepository.save(pregunta);

        // Crear opción asociada a la pregunta
        Opcion o = new Opcion("4", true);
        o.setPregunta(pregunta);
        opcionRepository.save(o);

        // Verificar que se guardó correctamente
        assertThat(opcionRepository.findAll()).isNotEmpty();
    }
}
