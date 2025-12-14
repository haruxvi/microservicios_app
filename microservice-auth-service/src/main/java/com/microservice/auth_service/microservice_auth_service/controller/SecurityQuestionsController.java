package com.microservice.auth_service.microservice_auth_service.controller;

import com.microservice.auth_service.microservice_auth_service.dto.SecurityQuestionDto;
import com.microservice.auth_service.microservice_auth_service.dto.SetupSecurityQuestionsRequest;
import com.microservice.auth_service.microservice_auth_service.service.SecurityQuestionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth/security-questions")
@Tag(
        name = "Preguntas de seguridad",
        description = "Gestión de preguntas de seguridad y configuración por usuario"
)
public class SecurityQuestionsController {

    private final SecurityQuestionsService service;

    public SecurityQuestionsController(SecurityQuestionsService service) {
        this.service = service;
    }

    @Operation(
            summary = "Listar preguntas de seguridad activas",
            description = "Devuelve el listado de preguntas de seguridad disponibles/activas para que el usuario pueda configurarlas."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado obtenido correctamente",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = SecurityQuestionDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @GetMapping
    public ResponseEntity<List<SecurityQuestionDto>> list() {
        return ResponseEntity.ok(service.listarActivas());
    }

    @Operation(
            summary = "Configurar preguntas de seguridad del usuario",
            description = "Permite registrar o actualizar las 3 preguntas de seguridad del usuario (y sus respuestas). " +
                    "Normalmente se usa al finalizar el registro o desde perfil/seguridad."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Preguntas configuradas correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Payload inválido (faltan campos, formato incorrecto, etc.)",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no existe o alguna pregunta no existe/está inactiva",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto al configurar preguntas (por ejemplo duplicadas o ya configuradas en una regla de negocio)",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @PostMapping("/setup")
    public ResponseEntity<Void> setup(@Valid @RequestBody SetupSecurityQuestionsRequest req) {
        service.setupPreguntas(req);
        return ResponseEntity.noContent().build(); // 204
    }
}
