package com.microservice.feedbak_service.repository;

import com.microservice.feedbak_service.model.Feedback;
import com.microservice.feedbak_service.repository.FeedbackRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")   // 👈 usa el application.properties de test
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // usa H2 en memoria
class FeedbackRepositoryTest {

    @Autowired
    private FeedbackRepository repository;

    @Test
    void debeGuardarYLeerFeedback() {
        Feedback f = new Feedback();
        f.setMensaje("Mensaje repo test");
        f.setTipo("ERROR");
        f.setDestino("ADMIN");
        f.setFecha(LocalDateTime.now());
        f.setResuelto(false);
        f.setUsuarioId(10L);

        repository.save(f);

        List<Feedback> todos = repository.findAll();

        assertThat(todos).isNotEmpty();
        Feedback guardado = todos.getFirst();
        assertThat(guardado.getMensaje()).isEqualTo("Mensaje repo test");
        assertThat(guardado.getUsuarioId()).isEqualTo(10L);
        assertThat(guardado.isResuelto()).isFalse();
    }

    @Test
    void debeEncontrarPendientesOrdenadosPorFechaDesc() {
        // given
        Feedback viejo = new Feedback();
        viejo.setMensaje("Viejo pendiente");
        viejo.setTipo("INFO");
        viejo.setDestino("ADMIN");
        viejo.setFecha(LocalDateTime.now().minusHours(2));
        viejo.setResuelto(false);
        viejo.setUsuarioId(1L);

        Feedback nuevo = new Feedback();
        nuevo.setMensaje("Nuevo pendiente");
        nuevo.setTipo("INFO");
        nuevo.setDestino("ADMIN");
        nuevo.setFecha(LocalDateTime.now());
        nuevo.setResuelto(false);
        nuevo.setUsuarioId(1L);

        Feedback resuelto = new Feedback();
        resuelto.setMensaje("Ya resuelto");
        resuelto.setTipo("ERROR");
        resuelto.setDestino("ADMIN");
        resuelto.setFecha(LocalDateTime.now().minusHours(1));
        resuelto.setResuelto(true);
        resuelto.setUsuarioId(1L);

        repository.saveAll(List.of(viejo, nuevo, resuelto));

        // when
        List<Feedback> pendientes = repository.findByResueltoFalseOrderByFechaDesc();

        // then
        assertThat(pendientes).hasSize(2);
        assertThat(pendientes.get(0).getMensaje()).isEqualTo("Nuevo pendiente");
        assertThat(pendientes.get(1).getMensaje()).isEqualTo("Viejo pendiente");
    }

    @Test
    void debeFiltrarPorUsuarioYOrdenarPorFechaDesc() {
        // given
        Feedback user1Antiguo = new Feedback();
        user1Antiguo.setMensaje("User 1 antiguo");
        user1Antiguo.setTipo("INFO");
        user1Antiguo.setDestino("QUIZ");
        user1Antiguo.setFecha(LocalDateTime.now().minusDays(1));
        user1Antiguo.setResuelto(false);
        user1Antiguo.setUsuarioId(1L);

        Feedback user1Nuevo = new Feedback();
        user1Nuevo.setMensaje("User 1 nuevo");
        user1Nuevo.setTipo("INFO");
        user1Nuevo.setDestino("QUIZ");
        user1Nuevo.setFecha(LocalDateTime.now());
        user1Nuevo.setResuelto(false);
        user1Nuevo.setUsuarioId(1L);

        Feedback user2 = new Feedback();
        user2.setMensaje("User 2");
        user2.setTipo("ERROR");
        user2.setDestino("ADMIN");
        user2.setFecha(LocalDateTime.now());
        user2.setResuelto(false);
        user2.setUsuarioId(2L);

        repository.saveAll(List.of(user1Antiguo, user1Nuevo, user2));

        // when
        List<Feedback> delUsuario1 = repository.findByUsuarioIdOrderByFechaDesc(1L);

        // then
        assertThat(delUsuario1).hasSize(2);
        assertThat(delUsuario1.get(0).getMensaje()).isEqualTo("User 1 nuevo");
        assertThat(delUsuario1.get(1).getMensaje()).isEqualTo("User 1 antiguo");
        assertThat(delUsuario1).allMatch(f -> f.getUsuarioId().equals(1L));
    }
}
