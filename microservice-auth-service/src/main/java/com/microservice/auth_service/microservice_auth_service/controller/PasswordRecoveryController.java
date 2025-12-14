package com.microservice.auth_service.microservice_auth_service.controller;

import com.microservice.auth_service.microservice_auth_service.dto.RecoveryQuestionsResponse;
import com.microservice.auth_service.microservice_auth_service.dto.ResetPasswordRequest;
import com.microservice.auth_service.microservice_auth_service.dto.VerifyRecoveryRequest;
import com.microservice.auth_service.microservice_auth_service.dto.VerifyRecoveryResponse;
import com.microservice.auth_service.microservice_auth_service.service.PasswordRecoveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/password-recovery")
@Tag(name = "Recuperación de contraseña", description = "Endpoints para recuperar la contraseña mediante preguntas de seguridad")
public class PasswordRecoveryController {

    private final PasswordRecoveryService service;

    public PasswordRecoveryController(PasswordRecoveryService service) {
        this.service = service;
    }

    @Operation(
            summary = "Obtener preguntas de recuperación",
            description = "Devuelve las 3 preguntas de seguridad configuradas para el usuario indicado. " +
                    "El identificador puede ser, por ejemplo, el correo o el username según tu implementación."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Preguntas de recuperación obtenidas correctamente",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RecoveryQuestionsResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Identificador inválido o vacío",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no existe o no tiene 3 preguntas configuradas",
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
    @GetMapping("/questions")
    public ResponseEntity<RecoveryQuestionsResponse> questions(
            @RequestParam("identificador")
            @NotBlank(message = "El identificador no puede estar vacío")
            String identificador
    ) {
        return ResponseEntity.ok(service.getQuestions(identificador));
    }

    @Operation(
            summary = "Verificar respuestas de recuperación",
            description = "Verifica las respuestas del usuario a las 3 preguntas de seguridad. " +
                    "Si son correctas, devuelve un token temporal para permitir el cambio de contraseña."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Respuestas correctas, token emitido",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = VerifyRecoveryResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Payload inválido o respuestas incorrectas",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no existe o no tiene 3 preguntas configuradas",
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
    @PostMapping("/verify")
    public ResponseEntity<VerifyRecoveryResponse> verify(@Valid @RequestBody VerifyRecoveryRequest req) {
        String token = service.verifyAnswers(req);
        return ResponseEntity.ok(new VerifyRecoveryResponse(token));
    }

    @Operation(
            summary = "Restablecer contraseña",
            description = "Cambia la contraseña del usuario utilizando un token temporal emitido previamente por el endpoint de verificación. " +
                    "Si el token es válido y la contraseña cumple requisitos, se actualiza la contraseña."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Contraseña actualizada correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Token inválido/expirado o contraseña inválida",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no existe",
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
    @PostMapping("/reset")
    public ResponseEntity<Void> reset(@Valid @RequestBody ResetPasswordRequest req) {
        service.resetPassword(req);
        return ResponseEntity.noContent().build();
    }
}
