package com.microservice.auth_service.microservice_auth_service.config;

import com.microservice.auth_service.microservice_auth_service.MicroserviceAuthServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MicroserviceAuthServiceApplication.class)
@ActiveProfiles("test")
class ConfigBeansTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Test
    void passwordEncoderBean_existe_yEncodea() {
        assertNotNull(passwordEncoder);
        String hashed = passwordEncoder.encode("123456");
        assertNotNull(hashed);
        assertNotEquals("123456", hashed);
        assertTrue(passwordEncoder.matches("123456", hashed));
    }

    @Test
    void securityFilterChainBean_existe() {
        assertNotNull(securityFilterChain);
    }
}
