package com.microservice.game_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "correo", nullable = false)
    private String correo;

    @Column(name = "clave", nullable = false)
    private String clave;

    @Lob
    @Column(name = "foto_perfil", nullable = false)
    private byte[] fotoPerfil;

    @ManyToOne
    @JoinColumn(name = "Rol_id_rol", nullable = false)
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "Estado_id_estado", nullable = false)
    private Estado estado;
}
