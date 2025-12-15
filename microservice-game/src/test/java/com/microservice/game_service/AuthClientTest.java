package com.microservice.game_service;

import com.microservice.game_service.service.AuthClient;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthClientTest {

    @Test
    void obtenerYActualizarPuntajeGlobal_usaWebClientCorrectamente() {
        // Simulamos respuestas distintas para GET y POST
        WebClient.Builder builder = WebClient.builder()
                .exchangeFunction(request -> {
                    boolean isPost = request.method().name().equals("POST");
                    int value = isPost ? 120 : 100;

                    ClientResponse response = ClientResponse
                            .create(HttpStatus.OK)
                            .header("Content-Type", "application/json")
                            .body(String.valueOf(value))
                            .build();

                    return Mono.just(response);
                });

        AuthClient client = new AuthClient(builder, "http://fake-auth");

        int actual = client.obtenerPuntajeGlobal(1L);
        int nuevo = client.actualizarPuntajeGlobal(1L, 20);

        assertEquals(100, actual);
        assertEquals(120, nuevo);
    }
}
