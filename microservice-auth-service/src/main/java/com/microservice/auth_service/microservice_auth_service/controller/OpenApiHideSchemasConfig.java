package com.microservice.auth_service.microservice_auth_service.controller;

import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class OpenApiHideSchemasConfig {

    private static final Set<String> SCHEMAS_A_OCULTAR = Set.of(
            "ContentDisposition",
            "ErrorResponse",
            "HttpMethod",
            "HttpRange",
            "HttpStatusCode",
            "MediaType",
            "ProblemDetail"
    );

    @Bean
    public OpenApiCustomizer hideUnwantedSchemasCustomizer() {
        return openApi -> {
            if (openApi.getComponents() == null || openApi.getComponents().getSchemas() == null) return;

            var schemas = openApi.getComponents().getSchemas();

            // elimina los schemas exactos
            SCHEMAS_A_OCULTAR.forEach(schemas::remove);

            // por si vienen variantes (algunas versiones generan más nombres)
            schemas.entrySet().removeIf(e -> {
                String name = e.getKey();
                return name != null && (
                        name.startsWith("Http") ||
                        name.equalsIgnoreCase("ProblemDetail") ||
                        name.equalsIgnoreCase("ErrorResponse") ||
                        name.equalsIgnoreCase("MediaType") ||
                        name.equalsIgnoreCase("ContentDisposition")
                );
            });
        };
    }
}