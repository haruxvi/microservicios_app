import com.example.quizapp.feedback.entity.Feedback;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FeedbackRepositoryTest {

    @Autowired private FeedbackRepository repository;

    @Test
    void findByResueltoFalse_DebeRetornarSoloPendientes() {
        Feedback f1 = new Feedback();
        f1.setMensaje("Bug"); f1.setTipo("BUG"); f1.setDestino("APP"); f1.setUsuarioId(1L); f1.setResuelto(false);
        Feedback f2 = new Feedback();
        f2.setMensaje("Ok"); f2.setTipo("OK"); f2.setDestino("APP"); f2.setUsuarioId(1L); f2.setResuelto(true);

        repository.saveAll(List.of(f1, f2));

        var pendientes = repository.findByResueltoFalseOrderByFechaDesc();

        assertThat(pendientes).hasSize(1);
        assertThat(pendientes.get(0).getMensaje()).isEqualTo("Bug");
    }
}