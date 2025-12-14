package com.microservice.auth_service.microservice_auth_service.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PasswordRecoveryTokenService {

    private static final long TTL_SECONDS = 10 * 60;

    private final Map<String, Entry> tokens = new ConcurrentHashMap<>();

    public String create(Long userId) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, new Entry(userId, Instant.now().plusSeconds(TTL_SECONDS)));
        return token;
    }

    public Long consume(String token) {
        Entry e = tokens.remove(token);
        if (e == null) throw new IllegalArgumentException("Token inválido");
        if (Instant.now().isAfter(e.expiresAt)) throw new IllegalArgumentException("Token expirado");
        return e.userId;
    }

    private record Entry(Long userId, Instant expiresAt) {}
}
