package com.microservice.feedbak_service.service;

import com.microservice.feedbak_service.dto.FeedbackRequest;
import com.microservice.feedbak_service.dto.FeedbackResponse;
import com.microservice.feedbak_service.model.Feedback;
import com.microservice.feedbak_service.repository.FeedbackRepository;
import com.microservice.feedbak_service.service.FeedbackService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @Mock
    private FeedbackRepository repository;

    @InjectMocks
    private FeedbackService service;

    private Feedback feedback;

    @BeforeEach
    void setUp() {
        feedback = new Feedback();
        feedback.setIdFeedback(1L);
        feedback.setUsuarioId(10L);
        feedback.setMensaje("Prueba mensaje");
        feedback.setTipo("ERROR");
        feedback.setDestino("ADMIN");
        feedback.setFecha(LocalDateTime.now());
        feedback.setResuelto(false);
    }

    @Test
    void crear_debeGuardarFeedbackCorrectamente() {
        // given
        FeedbackRequest request = new FeedbackRequest(
                10L,
                "Mensaje de prueba",
                "error",
                "admin"
        );

        when(repository.save(any(Feedback.class))).thenAnswer(invocation -> {
            Feedback f = invocation.getArgument(0);
            f.setIdFeedback(1L);
            return f;
        });

        // when
        FeedbackResponse response = service.crear(request);

        // then
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Mensaje de prueba", response.mensaje());
        assertEquals("ERROR", response.tipo());
        assertEquals("ADMIN", response.destino());
        assertEquals(10L, response.usuarioId());
        assertFalse(response.resuelto());
        assertNotNull(response.fecha());

        ArgumentCaptor<Feedback> captor = ArgumentCaptor.forClass(Feedback.class);
        verify(repository).save(captor.capture());
        Feedback guardado = captor.getValue();
        assertEquals("Mensaje de prueba", guardado.getMensaje());
        assertEquals("ERROR", guardado.getTipo());
        assertEquals("ADMIN", guardado.getDestino());
        assertEquals(10L, guardado.getUsuarioId());
        assertNotNull(guardado.getFecha());
    }

    @Test
    void todos_debeRetornarListaDeFeedbackResponse() {
        when(repository.findAll()).thenReturn(List.of(feedback));

        List<FeedbackResponse> lista = service.todos();

        assertEquals(1, lista.size());
        FeedbackResponse resp = lista.get(0);
        assertEquals(feedback.getIdFeedback(), resp.id());
        assertEquals(feedback.getMensaje(), resp.mensaje());
        assertEquals(feedback.getTipo(), resp.tipo());
        assertEquals(feedback.getDestino(), resp.destino());
    }

    @Test
    void pendientes_debeUsarRepositorioYMapearResultados() {
        when(repository.findByResueltoFalseOrderByFechaDesc())
                .thenReturn(List.of(feedback));

        List<FeedbackResponse> lista = service.pendientes();

        assertEquals(1, lista.size());
        assertFalse(lista.get(0).resuelto());
        verify(repository).findByResueltoFalseOrderByFechaDesc();
    }

    @Test
    void porUsuario_debeFiltrarPorUsuarioId() {
        Long usuarioId = 10L;
        when(repository.findByUsuarioIdOrderByFechaDesc(usuarioId))
                .thenReturn(List.of(feedback));

        List<FeedbackResponse> lista = service.porUsuario(usuarioId);

        assertEquals(1, lista.size());
        assertEquals(usuarioId, lista.get(0).usuarioId());
        verify(repository).findByUsuarioIdOrderByFechaDesc(usuarioId);
    }

    @Test
    void resolver_debeMarcarFeedbackComoResuelto() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.of(feedback));
        when(repository.save(any(Feedback.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FeedbackResponse resp = service.resolver(id);

        assertNotNull(resp);
        assertTrue(resp.resuelto());
        verify(repository).findById(id);
        verify(repository).save(any(Feedback.class));
    }

    @Test
    void resolver_debeLanzarExcepcionSiNoExiste() {
        Long id = 99L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.resolver(id));
        verify(repository).findById(id);
    }
}
