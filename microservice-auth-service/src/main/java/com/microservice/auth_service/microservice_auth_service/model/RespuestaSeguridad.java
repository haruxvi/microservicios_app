package com.microservice.auth_service.microservice_auth_service.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "respuesta_seguridad")
public class RespuestaSeguridad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_respuesta")
    private Long idRespuesta;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "pregunta_id", nullable = false)
    private Integer preguntaId;

    @Column(name = "respuesta_hash", nullable = false)
    private String respuestaHash;
    
    public Long getIdRespuesta() {return idRespuesta;}
    public void setIdRespuesta(Long idRespuesta) {this.idRespuesta = idRespuesta;}


    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Integer getPreguntaId() { return preguntaId; }
    public void setPreguntaId(Integer preguntaId) { this.preguntaId = preguntaId; }

    public String getRespuestaHash() { return respuestaHash; }
    public void setRespuestaHash(String respuestaHash) { this.respuestaHash = respuestaHash; }
}
