package com.microservice.auth_service.microservice_auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Petición para crear o actualizar un usuario en el sistema")
public class UsuarioRequest {

    @Schema(
        description = "Nombre completo del usuario",
        example = "Carlos Pérez"
    )
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @Schema(
        description = "Correo electrónico del usuario",
        example = "carlos.perez@example.com"
    )
    @Email(message = "El correo electrónico debe ser válido")
    @NotBlank(message = "El correo no puede estar vacío")
    private String correo;

    @Schema(
        description = "Contraseña del usuario (mínimo 6 caracteres)",
        example = "MiClaveSegura123"
    )
    @NotBlank(message = "La clave no puede estar vacía")
    @Size(min = 6, message = "La clave debe tener al menos 6 caracteres")
    private String clave;

    @Schema(
        description = "ID del rol asignado al usuario",
        example = "2"
    )
    @NotNull(message = "El ID del rol es obligatorio")
    private Long idRol;

    @Schema(
        description = "ID del estado actual del usuario",
        example = "1"
    )
    @NotNull(message = "El ID del estado es obligatorio")
    private Long idEstado;

    @Schema(
        description = "Puntaje actual del usuario dentro del sistema",
        example = "50"
    )
    private Integer puntaje;

    @Schema(
        description = "Puntaje acumulado total del usuario",
        example = "1200"
    )
    private Integer puntajeGlobal;

    @Schema(
        description = "Foto de perfil codificada en Base64. Puede ser null si no se envía o cadena vacía para limpiar la foto.",
        example = "iVBORw0KGgoAAAANSUhEUgAAAOE..."
    )
    private String fotoPerfilBase64;

    public UsuarioRequest() {
    }

    // Getters y setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }

    public Long getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Long idEstado) {
        this.idEstado = idEstado;
    }

    public Integer getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }

    public Integer getPuntajeGlobal() {
        return puntajeGlobal;
    }

    public void setPuntajeGlobal(Integer puntajeGlobal) {
        this.puntajeGlobal = puntajeGlobal;
    }

    public String getFotoPerfilBase64() { 
        return fotoPerfilBase64; 
    }

    public void setFotoPerfilBase64(String fotoPerfilBase64) { 
        this.fotoPerfilBase64 = fotoPerfilBase64; 
    }
}
