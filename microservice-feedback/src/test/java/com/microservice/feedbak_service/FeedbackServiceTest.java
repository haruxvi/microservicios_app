import com.example.quizapp.feedback.dto.FeedbackRequest;
import com.example.quizapp.feedback.entity.Feedback;
import com.example.quizapp.feedback.repository.FeedbackRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class FeedbackServiceTest {

    @Autowired private FeedbackService service;
    @MockBean private FeedbackRepository repository;

    @Test
    void crear_DebeGuardarYDevolverFeedback() {
        FeedbackRequest req = new FeedbackRequest(1L, "Test", "BUG", "APP");
        Feedback entity = new Feedback();
        entity.setIdFeedback(1L);
        entity.setMensaje("Test");
        entity.setTipo("BUG");
        entity.setDestino("APP");
        entity.setUsuarioId(1L);

        when(repository.save(any())).thenReturn(entity);

        var result = service.crear(req);

        assertNotNull(result);
        assertEquals("Test", result.mensaje());
        verify(repository).save(any());
    }

    @Test
    void resolver_DebeMarcarComoResuelto() {
        Feedback f = new Feedback();
        f.setIdFeedback(1L);
        f.setResuelto(false);

        when(repository.findById(1L)).thenReturn(Optional.of(f));
        when(repository.save(any())).thenReturn(f);

        var result = service.resolver(1L);
        assertTrue(result.resuelto());
    }
}