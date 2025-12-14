package com.microservice.game_service;

import com.microservice.game_service.dto.PreguntaResponse;
import com.microservice.game_service.service.QuizClient;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuizClientTest {

    @Test
    void obtenerPreguntas_devuelveListaDesdeQuizService() {
        String json = """
                [
                  {
                    "id": 1,
                    "enunciado": "¿2+2?",
                    "puntaje": 5,
                    "opciones": [
                      { "id": 10, "texto": "3", "correcta": false },
                      { "id": 11, "texto": "4", "correcta": true }
                    ]
                  }
                ]
                """;

        WebClient.Builder builder = WebClient.builder()
                .exchangeFunction(request -> {
                    ClientResponse response = ClientResponse
                            .create(HttpStatus.OK)
                            .header("Content-Type", "application/json")
                            .body(json)
                            .build();

                    return Mono.just(response);
                });

        QuizClient client = new QuizClient(builder, "http://fake-quiz");

        List<PreguntaResponse> preguntas = client.obtenerPreguntas(1L, 1L);

        assertNotNull(preguntas);
        assertEquals(1, preguntas.size());

        PreguntaResponse p = preguntas.get(0);
        assertEquals(1L, p.id());
        assertEquals("¿2+2?", p.enunciado());
        assertEquals(5, p.puntaje());
        assertEquals(2, p.opciones().size());
    }
}
