package com.microservice.quiz_service;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.microservice.quiz_service.MicroserviceQuizApplication;

import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@ActiveProfiles("test")
class MicroserviceQuizApplicationTests {

    @Test
    void contextLoads() {
        // Verifica que el contexto arranca
    }

    @Test
    void main_runs_without_errors() {
        try (MockedStatic<SpringApplication> springApp = mockStatic(SpringApplication.class)) {
            MicroserviceQuizApplication.main(new String[]{});
        }
    }
}
