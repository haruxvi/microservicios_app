package com.microservice.feedbak_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_feedback")
    private Long idFeedback;

    @Column(name = "mensaje", nullable = false, length = 255)
    private String mensaje;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    // "QUIZ", "ADMIN", "AMBOS"
    @Column(name = "destino", nullable = false, length = 50)
    private String destino;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    // 0 = pendiente, 1 = resuelto
    @Column(name = "resuelto", nullable = false)
    private boolean resuelto = false;

    @Column(name = "Usuario_id_usuario", nullable = false)
    private Long usuarioId;

    // ====== Getters y setters ======

    public Long getIdFeedback() {
        return idFeedback;
    }

    public void setIdFeedback(Long idFeedback) {
        this.idFeedback = idFeedback;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public boolean isResuelto() {
        return resuelto;
    }

    public void setResuelto(boolean resuelto) {
        this.resuelto = resuelto;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
