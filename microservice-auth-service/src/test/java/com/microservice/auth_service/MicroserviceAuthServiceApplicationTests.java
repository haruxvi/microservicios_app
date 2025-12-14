package com.microservice.auth_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.microservice.auth_service.microservice_auth_service.MicroserviceAuthServiceApplication;

@SpringBootTest(classes = MicroserviceAuthServiceApplication.class)
class MicroserviceAuthServiceApplicationTests {

    @Test
    void contextLoads() {
        // Si el contexto levanta, este test pasa.
    }
}

