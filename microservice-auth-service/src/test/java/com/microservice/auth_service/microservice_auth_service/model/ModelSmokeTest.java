package com.microservice.auth_service.microservice_auth_service.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Smoke tests para ejecutar getters/setters/constructores de entidades.
 * No intenta validar JPA (eso ya lo cubren los @DataJpaTest).
 */
class ModelSmokeTest {

    @Test
    void estado_gettersSetters_yCtor() {
        Estado e = new Estado("ACTIVO");
        e.setId(1L);

        assertEquals(1L, e.getId());
        assertEquals("ACTIVO", e.getNombre());

        e.setNombre("INACTIVO");
        assertEquals("INACTIVO", e.getNombre());
    }

    @Test
    void rol_gettersSetters_yCtor() {
        Rol r = new Rol("ADMIN");
        r.setId(2L);

        assertEquals(2L, r.getId());
        assertEquals("ADMIN", r.getNombre());

        r.setNombre("USER");
        assertEquals("USER", r.getNombre());
    }

    @Test
    void usuario_gettersSetters_yCtor_actualizado() {
        Rol rol = new Rol("ADMIN");
        rol.setId(1L);

        Estado estado = new Estado("ACTIVO");
        estado.setId(1L);

        byte[] foto = new byte[]{1, 2, 3};

        // Constructor actualizado (sin puntajes en args)
        Usuario u = new Usuario("Juan", "juan@example.com", "hash", foto, rol, estado);

        assertEquals("Juan", u.getNombre());
        assertEquals("juan@example.com", u.getCorreo());
        assertEquals("hash", u.getClave());
        assertArrayEquals(foto, u.getFotoPerfil());
        assertEquals(rol, u.getRol());
        assertEquals(estado, u.getEstado());

        // defaults
        assertEquals(0, u.getPuntaje());
        assertEquals(0, u.getPuntajeGlobal());

        // setters
        u.setId(5L);
        u.setPuntaje(10);
        u.setPuntajeGlobal(100);
        u.setPregunta1Id(1);
        u.setPregunta2Id(3);
        u.setPregunta3Id(5);

        assertEquals(5L, u.getId());
        assertEquals(10, u.getPuntaje());
        assertEquals(100, u.getPuntajeGlobal());
        assertEquals(1, u.getPregunta1Id());
        assertEquals(3, u.getPregunta2Id());
        assertEquals(5, u.getPregunta3Id());
        assertTrue(u.tienePreguntasConfiguradas());
    }

    @Test
    void preguntaSeguridad_gettersSetters() {
        PreguntaSeguridad p = new PreguntaSeguridad();
        p.setId(1);
        p.setTexto("Pregunta?");
        p.setActiva(true);

        assertEquals(1, p.getId());
        assertEquals("Pregunta?", p.getTexto());
        assertTrue(p.getActiva());
    }

    @Test
    void respuestaSeguridad_gettersSetters() {
        RespuestaSeguridad r = new RespuestaSeguridad();

        r.setIdRespuesta(10L);
        r.setUsuarioId(5L);
        r.setPreguntaId(1);
        r.setRespuestaHash("hash");

        assertEquals(10L, r.getIdRespuesta());
        assertEquals(5L, r.getUsuarioId());
        assertEquals(1, r.getPreguntaId());
        assertEquals("hash", r.getRespuestaHash());
    }

}
