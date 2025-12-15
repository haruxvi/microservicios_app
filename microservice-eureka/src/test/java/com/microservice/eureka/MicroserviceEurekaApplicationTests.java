package com.microservice.eureka;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.mockStatic;

@SpringBootTest
class MicroserviceEurekaApplicationTests {

    @Test
    void contextLoads() {
        // Verifica que el contexto arranca sin problemas
    }

    @Test
    void main_runs_without_errors() {
        // Mockeamos SpringApplication.run para que NO arranque el servidor real
        try (MockedStatic<SpringApplication> springApp = mockStatic(SpringApplication.class)) {
            MicroserviceEurekaApplication.main(new String[]{}); // ejecuta la línea del main
            // No hace falta verificar nada, con llamarlo basta para cobertura
        }
    }
}
