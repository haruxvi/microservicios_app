package com.microservice.game_service;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.microservice.game_service.MicroserviceGameApplication;

import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@ActiveProfiles("test")
class MicroserviceGameApplicationTests {

    @Test
    void contextLoads() {
        // Solo arranca el contexto
    }

    @Test
    void main_runs_without_errors() {
        // Mockeamos SpringApplication.run para no levantar el servidor real
        try (MockedStatic<SpringApplication> springApp = mockStatic(SpringApplication.class)) {
            MicroserviceGameApplication.main(new String[]{});
        }
    }
}
