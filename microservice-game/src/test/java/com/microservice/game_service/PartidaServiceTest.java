package com.microservice.game_service;

import com.microservice.game_service.dto.*;
import com.microservice.game_service.model.Partida;
import com.microservice.game_service.repository.PartidaRepository;
import com.microservice.game_service.service.AuthClient;
import com.microservice.game_service.service.PartidaService;
import com.microservice.game_service.service.QuizClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartidaServiceTest {

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private QuizClient quizClient;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private PartidaService partidaService;

    private Partida partidaExistente;

    @BeforeEach
    void setUp() {
        // Partida de ejemplo usada en varios tests
        partidaExistente = new Partida(1L, 2L, 3L, 1L);
        partidaExistente.setIdPartida(50L);
        partidaExistente.setFCreacion(LocalDateTime.now().minusMinutes(10));
        partidaExistente.setPuntTotal(0);
    }

    @Test
    void iniciarPartida_debeCrearPartidaYRetornarPreguntas() {
        // Arrange
        IniciarPartidaRequest request = new IniciarPartidaRequest(1L, 2L, 3L);

        List<PreguntaResponse> preguntasMock = List.of(
                new PreguntaResponse(10L, "¿2+2?", 5, List.of())
        );

        when(quizClient.obtenerPreguntas(2L, 3L)).thenReturn(preguntasMock);
        when(partidaRepository.save(any(Partida.class))).thenAnswer(invocation -> {
            Partida p = invocation.getArgument(0);
            p.setIdPartida(100L);
            return p;
        });

        // Act
        IniciarPartidaResponse response = partidaService.iniciarPartida(request);

        // Assert
        assertNotNull(response);
        assertEquals(100L, response.partidaId());
        assertNotNull(response.fechaInicio());
        assertEquals(1, response.preguntas().size());
        assertEquals("¿2+2?", response.preguntas().get(0).enunciado());

        // Verificar que se llamó al repo y al cliente de quiz
        ArgumentCaptor<Partida> captor = ArgumentCaptor.forClass(Partida.class);
        verify(partidaRepository).save(captor.capture());
        Partida guardada = captor.getValue();

        assertEquals(1L, guardada.getUsuarioId());
        assertEquals(2L, guardada.getCategoriaId());
        assertEquals(3L, guardada.getDificultadId());
        assertEquals(0, guardada.getPuntTotal());

        verify(quizClient).obtenerPreguntas(2L, 3L);
    }

    @Test
    void finalizarPartida_debeActualizarPuntajeYEstado() {
        // Arrange
        FinalizarPartidaRequest request = new FinalizarPartidaRequest(50L, 20);

        when(partidaRepository.findById(50L)).thenReturn(Optional.of(partidaExistente));
        when(authClient.obtenerPuntajeGlobal(1L)).thenReturn(100);
        when(authClient.actualizarPuntajeGlobal(1L, 20)).thenReturn(120);
        when(partidaRepository.save(any(Partida.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        FinalizarPartidaResponse response = partidaService.finalizarPartida(request);

        // Assert
        assertNotNull(response);
        assertEquals(50L, response.partidaId());
        assertEquals(20, response.puntajeObtenido());
        assertEquals(100, response.puntajeAnteriorGlobal());
        assertEquals(120, response.puntajeNuevoGlobal());
        assertNotNull(response.fechaFin());
        assertTrue(response.mensaje().toLowerCase().contains("finalizada"));

        // Verificar cambios en la entidad guardada
        ArgumentCaptor<Partida> captor = ArgumentCaptor.forClass(Partida.class);
        verify(partidaRepository).save(captor.capture());
        Partida actualizada = captor.getValue();

        assertEquals(20, actualizada.getPuntTotal());
        assertNotNull(actualizada.getFFinalizacion());
        assertEquals(2L, actualizada.getEstadoId()); // ESTADO_FINALIZADA_ID en el service

        verify(authClient).obtenerPuntajeGlobal(1L);
        verify(authClient).actualizarPuntajeGlobal(1L, 20);
    }

    @Test
    void finalizarPartida_debeLanzarExcepcionSiPartidaNoExiste() {
        // Arrange
        FinalizarPartidaRequest request = new FinalizarPartidaRequest(999L, 10);
        when(partidaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> partidaService.finalizarPartida(request));
        verify(partidaRepository, never()).save(any());
    }

    @Test
    void obtenerPartidasPorUsuario_debeRetornarListaMapeada() {
        // Arrange
        partidaExistente.setFFinalizacion(LocalDateTime.now());
        partidaExistente.setPuntTotal(50);

        when(partidaRepository.findByUsuarioId(1L))
                .thenReturn(List.of(partidaExistente));

        // Act
        List<PartidaResponse> lista = partidaService.obtenerPartidasPorUsuario(1L);

        // Assert
        assertNotNull(lista);
        assertEquals(1, lista.size());

        PartidaResponse resp = lista.get(0);
        assertEquals(50L, resp.id());
        assertEquals(1L, resp.usuarioId());
        assertEquals(50, resp.puntajeFinal());
        assertNotNull(resp.fechaInicio());
        assertNotNull(resp.fechaFin());
        assertNotNull(resp.categoria());
        assertNotNull(resp.dificultad());
        assertNotNull(resp.estado());
    }

        @Test
    void obtenerPartidasPorUsuario_sinResultadosDebeRetornarListaVacia() {
        // Arrange
        Long usuarioIdSinPartidas = 999L;
        when(partidaRepository.findByUsuarioId(usuarioIdSinPartidas))
                .thenReturn(List.of()); // lista vacía

        // Act
        List<PartidaResponse> resultado = partidaService.obtenerPartidasPorUsuario(usuarioIdSinPartidas);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        // Verificar que solo se llamó al repository y nada más
        verify(partidaRepository).findByUsuarioId(usuarioIdSinPartidas);
        verifyNoMoreInteractions(partidaRepository, quizClient, authClient);
    }

}
