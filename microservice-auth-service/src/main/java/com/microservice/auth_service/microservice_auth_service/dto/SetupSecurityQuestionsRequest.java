package com.microservice.auth_service.microservice_auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Schema(description = "Petición para configurar (registrar o actualizar) las preguntas de seguridad de un usuario")
public class SetupSecurityQuestionsRequest {

    @Schema(description = "ID del usuario a configurar", example = "12")
    @NotNull(message = "El userId es obligatorio")
    private Long userId;

    @Schema(description = "Listado de preguntas y respuestas (normalmente 3 ítems)")
    @NotEmpty(message = "Debe enviar al menos una pregunta con su respuesta")
    @Valid
    private List<Item> items;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }

    @Schema(description = "Ítem de configuración: pregunta seleccionada + respuesta")
    public static class Item {

        @Schema(description = "ID de la pregunta seleccionada", example = "5")
        @NotNull(message = "El preguntaId es obligatorio")
        private Integer preguntaId;

        @Schema(description = "Respuesta del usuario", example = "Mi colegio fue San Pedro")
        @NotBlank(message = "La respuesta no puede estar vacía")
        private String respuesta;

        public Integer getPreguntaId() { return preguntaId; }
        public void setPreguntaId(Integer preguntaId) { this.preguntaId = preguntaId; }

        public String getRespuesta() { return respuesta; }
        public void setRespuesta(String respuesta) { this.respuesta = respuesta; }
    }
}
