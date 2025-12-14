package com.microservice.quiz_service.services;

import com.microservice.quiz_service.dto.OpcionRequest;
import com.microservice.quiz_service.dto.PreguntaRequest;
import com.microservice.quiz_service.dto.PreguntaResponse;
import com.microservice.quiz_service.model.Categoria;
import com.microservice.quiz_service.model.Dificultad;
import com.microservice.quiz_service.model.Estado;
import com.microservice.quiz_service.model.Opcion;
import com.microservice.quiz_service.model.Pregunta;
import com.microservice.quiz_service.repository.CategoriaRepository;
import com.microservice.quiz_service.repository.DificultadRepository;
import com.microservice.quiz_service.repository.EstadoRepository;
import com.microservice.quiz_service.repository.PreguntaRepository;
import com.microservice.quiz_service.services.PreguntaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PreguntaServiceTest {

    @Mock
    private PreguntaRepository preguntaRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private DificultadRepository dificultadRepository;

    @Mock
    private EstadoRepository estadoRepository;

    @InjectMocks
    private PreguntaService preguntaService;

    private Categoria categoria;
    private Dificultad dificultad;
    private Estado estado;
    private Pregunta pregunta;

    @BeforeEach
    void setUp() {
        categoria = new Categoria("Matemáticas");
        categoria.setId(1L);

        dificultad = new Dificultad("Fácil");
        dificultad.setId(1L);

        estado = new Estado("ACTIVA");
        estado.setId(1L);

        pregunta = new Pregunta("¿2+2?", categoria, dificultad, estado);
        pregunta.setId(10L);

        Opcion o1 = new Opcion("3", false);
        o1.setId(100L);

        Opcion o2 = new Opcion("4", true);
        o2.setId(101L);

        // ✅ Usar el método que ya tiene la entidad; su lista interna es mutable
        pregunta.addOpcion(o1);
        pregunta.addOpcion(o2);
    }


    @Test
    void create_debeCrearPreguntaConOpciones() {
        PreguntaRequest request = new PreguntaRequest();
        request.setEnunciado("¿2+2?");
        request.setIdCategoria(1L);
        request.setIdDificultad(1L);
        request.setIdEstado(1L);
        request.setOpciones(List.of(
                new OpcionRequest("3", false),
                new OpcionRequest("4", true)
        ));

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(dificultadRepository.findById(1L)).thenReturn(Optional.of(dificultad));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));
        when(preguntaRepository.save(any(Pregunta.class))).thenAnswer(inv -> {
            Pregunta p = inv.getArgument(0);
            p.setId(10L);
            // simular IDs en opciones
            long baseId = 100L;
            for (int i = 0; i < p.getOpciones().size(); i++) {
                p.getOpciones().get(i).setId(baseId + i);
            }
            return p;
        });

        PreguntaResponse resp = preguntaService.create(request);

        assertNotNull(resp);
        assertEquals(10L, resp.getId());
        assertEquals("¿2+2?", resp.getEnunciado());
        assertEquals("Matemáticas", resp.getCategoria());
        assertEquals("Fácil", resp.getDificultad());
        assertEquals("ACTIVA", resp.getEstado());
        assertEquals(2, resp.getOpciones().size());

        ArgumentCaptor<Pregunta> captor = ArgumentCaptor.forClass(Pregunta.class);
        verify(preguntaRepository).save(captor.capture());
        Pregunta guardada = captor.getValue();
        assertEquals("¿2+2?", guardada.getEnunciado());
        assertEquals(2, guardada.getOpciones().size());
    }

    @Test
    void getAll_debeRetornarListadoDePreguntas() {
        when(preguntaRepository.findAll()).thenReturn(List.of(pregunta));

        List<PreguntaResponse> lista = preguntaService.getAll();

        assertEquals(1, lista.size());
        assertEquals("¿2+2?", lista.get(0).getEnunciado());
    }

    @Test
    void getById_debeRetornarPregunta() {
        when(preguntaRepository.findById(10L)).thenReturn(Optional.of(pregunta));

        PreguntaResponse resp = preguntaService.getById(10L);

        assertEquals(10L, resp.getId());
        assertEquals("¿2+2?", resp.getEnunciado());
    }

    @Test
    void getByCategoria_debeRetornarPreguntasFiltradas() {
        when(preguntaRepository.findByCategoria_Id(1L)).thenReturn(List.of(pregunta));

        List<PreguntaResponse> lista = preguntaService.getByCategoria(1L);

        assertEquals(1, lista.size());
        assertEquals("Matemáticas", lista.get(0).getCategoria());
    }

    @Test
    void delete_debeEliminarPregunta() {
        when(preguntaRepository.existsById(10L)).thenReturn(true);

        preguntaService.delete(10L);

        verify(preguntaRepository).deleteById(10L);
    }

        @Test
    void getById_debeLanzarExcepcionSiNoExiste() {
        when(preguntaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> preguntaService.getById(99L));
    }

    @Test
    void create_debeFallarSiCategoriaNoExiste() {
        PreguntaRequest request = new PreguntaRequest();
        request.setEnunciado("Pregunta X");
        request.setIdCategoria(99L);
        request.setIdDificultad(1L);
        request.setIdEstado(1L);
        request.setOpciones(List.of(new OpcionRequest("op", true)));

        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> preguntaService.create(request));

        verify(categoriaRepository).findById(99L);
        verifyNoMoreInteractions(dificultadRepository, estadoRepository, preguntaRepository);
    }

    @Test
    void create_debeFallarSiNoTieneOpciones() {
        PreguntaRequest request = new PreguntaRequest();
        request.setEnunciado("Pregunta sin opciones");
        request.setIdCategoria(1L);
        request.setIdDificultad(1L);
        request.setIdEstado(1L);
        request.setOpciones(List.of()); // lista vacía

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(dificultadRepository.findById(1L)).thenReturn(Optional.of(dificultad));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));

        assertThrows(RuntimeException.class, () -> preguntaService.create(request));

        verify(preguntaRepository, never()).save(any());
    }

    @Test
    void delete_debeLanzarExcepcionSiNoExiste() {
        when(preguntaRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> preguntaService.delete(99L));

        verify(preguntaRepository).existsById(99L);
        verify(preguntaRepository, never()).deleteById(anyLong());
    }

        @Test
    void getByDificultad_debeRetornarPreguntasFiltradas() {
        when(preguntaRepository.findByDificultad_Id(1L))
                .thenReturn(List.of(pregunta));

        var resultado = preguntaService.getByDificultad(1L);

        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).getId());
        assertEquals("¿2+2?", resultado.get(0).getEnunciado());

        verify(preguntaRepository).findByDificultad_Id(1L);
    }

    @Test
    void getByCategoriaAndDificultad_debeRetornarPreguntasFiltradas() {
        when(preguntaRepository.findByCategoria_IdAndDificultad_Id(1L, 1L))
                .thenReturn(List.of(pregunta));

        var resultado = preguntaService.getByCategoriaAndDificultad(1L, 1L);

        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).getId());
        assertEquals("¿2+2?", resultado.get(0).getEnunciado());

        verify(preguntaRepository)
                .findByCategoria_IdAndDificultad_Id(1L, 1L);
    }

    @Test
    void update_debeActualizarPreguntaYOpciones() {
        PreguntaRequest request = new PreguntaRequest();
        request.setEnunciado("¿3+3?");
        request.setIdCategoria(1L);
        request.setIdDificultad(1L);
        request.setIdEstado(1L);
        request.setOpciones(List.of(
                new OpcionRequest("5", false),
                new OpcionRequest("6", true)
        ));

        when(preguntaRepository.findById(10L)).thenReturn(Optional.of(pregunta));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(dificultadRepository.findById(1L)).thenReturn(Optional.of(dificultad));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));
        when(preguntaRepository.save(any(Pregunta.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        PreguntaResponse resp = preguntaService.update(10L, request);

        assertEquals(10L, resp.getId());
        assertEquals("¿3+3?", resp.getEnunciado());
        assertEquals(2, resp.getOpciones().size());
        assertEquals("5", resp.getOpciones().get(0).getTexto());
        assertEquals("6", resp.getOpciones().get(1).getTexto());

        verify(preguntaRepository).findById(10L);
        verify(preguntaRepository).save(any(Pregunta.class));
    }

    @Test
    void update_debeLanzarExcepcionSiPreguntaNoExiste() {
        PreguntaRequest request = new PreguntaRequest();
        request.setEnunciado("cualquiera");
        request.setIdCategoria(1L);
        request.setIdDificultad(1L);
        request.setIdEstado(1L);
        request.setOpciones(List.of(new OpcionRequest("x", true)));

        when(preguntaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> preguntaService.update(99L, request));

        verify(preguntaRepository).findById(99L);
        verify(preguntaRepository, never()).save(any());
    }

    @Test
    void update_debeLanzarExcepcionSiNoTieneOpciones() {
        PreguntaRequest request = new PreguntaRequest();
        request.setEnunciado("¿4+4?");
        request.setIdCategoria(1L);
        request.setIdDificultad(1L);
        request.setIdEstado(1L);
        request.setOpciones(List.of()); // lista vacía

        when(preguntaRepository.findById(10L)).thenReturn(Optional.of(pregunta));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(dificultadRepository.findById(1L)).thenReturn(Optional.of(dificultad));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));

        assertThrows(RuntimeException.class,
                () -> preguntaService.update(10L, request));

        verify(preguntaRepository).findById(10L);
        verify(preguntaRepository, never()).save(any());
    }


}
