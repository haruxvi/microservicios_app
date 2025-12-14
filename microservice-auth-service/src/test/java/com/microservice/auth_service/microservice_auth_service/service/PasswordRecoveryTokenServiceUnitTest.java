package com.microservice.auth_service.microservice_auth_service.service;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PasswordRecoveryTokenServiceUnitTest {

    @Test
    void create_y_consume_devuelveUserId() {
        PasswordRecoveryTokenService svc = new PasswordRecoveryTokenService();
        String token = svc.create(99L);

        Long userId = svc.consume(token);

        assertEquals(99L, userId);
    }

    @Test
    void consume_tokenInvalido_lanzaIllegalArgument() {
        PasswordRecoveryTokenService svc = new PasswordRecoveryTokenService();
        var ex = assertThrows(IllegalArgumentException.class, () -> svc.consume("no-existe"));
        assertTrue(ex.getMessage().toLowerCase().contains("inválido") || ex.getMessage().toLowerCase().contains("inval"));
    }

    @Test
    void consume_tokenExpirado_lanzaIllegalArgument() throws Exception {
        PasswordRecoveryTokenService svc = new PasswordRecoveryTokenService();

        // Accedemos al mapa interno y forzamos una entrada expirada.
        Field tokensField = PasswordRecoveryTokenService.class.getDeclaredField("tokens");
        tokensField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> tokens = (Map<String, Object>) tokensField.get(svc);

        Class<?> entryClass = PasswordRecoveryTokenService.class.getDeclaredClasses()[0]; // Entry record
        Constructor<?> ctor = entryClass.getDeclaredConstructor(Long.class, Instant.class);
        ctor.setAccessible(true);

        String token = "expired-token";
        Object entry = ctor.newInstance(1L, Instant.now().minusSeconds(5));
        tokens.put(token, entry);

        var ex = assertThrows(IllegalArgumentException.class, () -> svc.consume(token));
        assertTrue(ex.getMessage().toLowerCase().contains("expir"));
    }
}
