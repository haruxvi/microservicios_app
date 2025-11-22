package com.microservice.game_service;



import com.microservice.game_service.dto.FinalizarPartidaRequest;
import com.microservice.game_service.dto.IniciarPartidaRequest;
import com.microservice.game_service.dto.PreguntaResponse;
import com.microservice.game_service.dto.OpcionResponse;
import com.microservice.game_service.model.Partida;
import com.microservice.game_service.repository.PartidaRepository;
import com.microservice.game_service.service.AuthClient;
import com.microservice.game_service.service.PartidaService;
import com.microservice.game_service.service.QuizClient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartidaServiceUnitTest {

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private QuizClient quizClient;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private PartidaService partidaService;

    @Captor
    private ArgumentCaptor<Partida> partidaCaptor;

    @SuppressWarnings("null")
	@Test
    void iniciarPartida_CreaPartidaYObtienePreguntas() {
        // Arrange
        IniciarPartidaRequest req = new IniciarPartidaRequest(10L, 20L, 30L);
        when(partidaRepository.save(any())).thenAnswer(invocation -> {
            Partida p = invocation.getArgument(0);
            p.setIdPartida(123L);
            return p;
        });
        PreguntaResponse pregunta = new PreguntaResponse(1L, "¿Cuál?", 5, List.of(new OpcionResponse(1L, "A", false)));
        when(quizClient.obtenerPreguntas(20L, 30L)).thenReturn(List.of(pregunta));

        // Act
        var resp = partidaService.iniciarPartida(req);

        // Assert
        verify(partidaRepository).save(partidaCaptor.capture());
        Partida saved = partidaCaptor.getValue();
        assertThat(saved.getFCreacion()).isNotNull();
        assertThat(saved.getUsuarioId()).isEqualTo(10L);
        assertThat(saved.getCategoriaId()).isEqualTo(20L);
        assertThat(saved.getDificultadId()).isEqualTo(30L);

        assertThat(resp).isNotNull();
        assertThat(resp.partidaId()).isEqualTo(123L);
        assertThat(resp.preguntas()).isNotEmpty();

        verify(quizClient).obtenerPreguntas(20L, 30L);
    }

    @SuppressWarnings("null")
	@Test
    void finalizarPartida_CuandoExiste_ActualizaYSumaPuntaje() {
        // Arrange
        Partida partida = new Partida();
        partida.setIdPartida(1L);
        partida.setUsuarioId(55L);
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));

        FinalizarPartidaRequest req = new FinalizarPartidaRequest(1L, 99);

        // Act
        String res = partidaService.finalizarPartida(req);

        // Assert
        verify(partidaRepository).save(partidaCaptor.capture());
        Partida saved = partidaCaptor.getValue();
        assertThat(saved.getFFinalizacion()).isNotNull();
        assertThat(saved.getPuntTotal()).isEqualTo(99);

        verify(authClient).sumarPuntaje(55L, 99);
        assertThat(res).contains("99");
    }

    @Test
    void finalizarPartida_CuandoNoExiste_LanzaException() {
        when(partidaRepository.findById(999L)).thenReturn(Optional.empty());
        FinalizarPartidaRequest req = new FinalizarPartidaRequest(999L, 0);
        assertThatThrownBy(() -> partidaService.finalizarPartida(req))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Partida no encontrada");
    }
}