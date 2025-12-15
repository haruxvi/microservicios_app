package com.microservice.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MicroserviceConfigApplicationTests {

    @Test
    void contextLoads() {
        // Verifica que el contexto levanta bien el Config Server
    }

    @Test
    void main_runs_without_errors() {
        MicroserviceConfigApplication.main(
                new String[] { "--spring.main.web-application-type=none" }
        );
    }
}
