package com.microservice.auth_service.microservice_auth_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "correo", nullable = false, unique = true, length = 200)
    private String correo;

    @Column(name = "clave", nullable = false, length = 255)
    private String clave;

    @Lob
    @Column(name = "foto_perfil")
    private byte[] fotoPerfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id_rol", nullable = false)
    private Rol rol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id_estado", nullable = false)
    private Estado estado;

    @Column(name = "puntaje")
    private Integer puntaje = 0;

    @Column(name = "puntaje_global")
    private Integer puntajeGlobal = 0;

    public Usuario() {
    }

    public Usuario(String nombre, String correo, String clave, byte[] fotoPerfil,
                   Rol rol, Estado estado, Integer puntaje, Integer puntajeGlobal) {
        this.nombre = nombre;
        this.correo = correo;
        this.clave = clave;
        this.fotoPerfil = fotoPerfil;
        this.rol = rol;
        this.estado = estado;
        this.puntaje = puntaje;
        this.puntajeGlobal = puntajeGlobal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
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
}
