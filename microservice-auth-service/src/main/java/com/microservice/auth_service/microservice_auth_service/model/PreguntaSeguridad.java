package com.microservice.auth_service.microservice_auth_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pregunta_seguridad")
public class PreguntaSeguridad {

    @Id
    @Column(name = "id_pregunta")
    private Integer id;


    @Column(nullable = false, length = 255)
    private String texto;

    @Column(nullable = false)
    private Boolean activa = true;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }
}
