package com.microservice.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.mockStatic;

@SpringBootTest
class MicroserviceGatewayApplicationTests {

    @Test
    void contextLoads() {
        // Verifica que el contexto del gateway se levanta bien
    }

    @Test
    void main_runs_without_errors() {
        try (MockedStatic<SpringApplication> springApp = mockStatic(SpringApplication.class)) {
            MicroserviceGatewayApplication.main(new String[]{}); // ejecuta la línea del main
        }
    }
}
