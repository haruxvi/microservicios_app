package com.microservice.auth_service.microservice_auth_service.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Smoke tests para subir cobertura de DTOs (getters/setters/records).
 * No valida Bean Validation: solo asegura que el código de DTOs se ejecuta.
 */
class DtoSmokeTest {

    @Test
    void loginRequest_record_funciona() {
        LoginRequest r1 = new LoginRequest("mail@example.com", "1234");
        LoginRequest r2 = new LoginRequest("mail@example.com", "1234");

        assertEquals("mail@example.com", r1.identificador());
        assertEquals("1234", r1.clave());
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertTrue(r1.toString().contains("mail@example.com"));
    }

    @Test
    void actualizarFotoPerfilRequest_record_funciona() {
        ActualizarFotoPerfilRequest r1 = new ActualizarFotoPerfilRequest("aW1n");
        ActualizarFotoPerfilRequest r2 = new ActualizarFotoPerfilRequest("aW1n");

        assertEquals("aW1n", r1.fotoBase64());
        assertEquals(r1, r2);
        assertTrue(r1.toString().contains("aW1n"));
    }

    @Test
    void usuarioRequest_gettersSetters() {
        UsuarioRequest req = new UsuarioRequest();
        req.setNombre("Juan");
        req.setCorreo("juan@example.com");
        req.setClave("123456");
        req.setIdRol(1L);
        req.setIdEstado(2L);
        req.setPuntaje(10);
        req.setPuntajeGlobal(100);
        req.setFotoPerfilBase64("aW1n");

        assertEquals("Juan", req.getNombre());
        assertEquals("juan@example.com", req.getCorreo());
        assertEquals("123456", req.getClave());
        assertEquals(1L, req.getIdRol());
        assertEquals(2L, req.getIdEstado());
        assertEquals(10, req.getPuntaje());
        assertEquals(100, req.getPuntajeGlobal());
        assertEquals("aW1n", req.getFotoPerfilBase64());
    }

    @Test
    void usuarioResponse_gettersSetters_yCtor() {
        UsuarioResponse resp = new UsuarioResponse(1L, "Juan", "juan@example.com",
                "ADMIN", "ACTIVO", 10, 100, "aW1n");
        assertEquals(1L, resp.getId());
        assertEquals("Juan", resp.getNombre());
        assertEquals("juan@example.com", resp.getCorreo());
        assertEquals("ADMIN", resp.getRol());
        assertEquals("ACTIVO", resp.getEstado());
        assertEquals(10, resp.getPuntaje());
        assertEquals(100, resp.getPuntajeGlobal());
        assertEquals("aW1n", resp.getFotoPerfilBase64());

        UsuarioResponse resp2 = new UsuarioResponse();
        resp2.setId(2L);
        resp2.setNombre("Ana");
        resp2.setCorreo("ana@example.com");
        resp2.setRol("USER");
        resp2.setEstado("ACTIVO");
        resp2.setPuntaje(0);
        resp2.setPuntajeGlobal(0);
        resp2.setFotoPerfilBase64(null);

        assertEquals(2L, resp2.getId());
        assertEquals("Ana", resp2.getNombre());
        assertEquals("ana@example.com", resp2.getCorreo());
        assertEquals("USER", resp2.getRol());
        assertEquals("ACTIVO", resp2.getEstado());
        assertEquals(0, resp2.getPuntaje());
        assertEquals(0, resp2.getPuntajeGlobal());
        assertNull(resp2.getFotoPerfilBase64());
    }

    @Test
    void estadoRequestResponse_gettersSetters_yCtors() {
        EstadoRequest er = new EstadoRequest("ACTIVO");
        assertEquals("ACTIVO", er.getNombre());
        er.setNombre("SUSPENDIDO");
        assertEquals("SUSPENDIDO", er.getNombre());

        EstadoResponse resp = new EstadoResponse(1L, "ACTIVO");
        assertEquals(1L, resp.getId());
        assertEquals("ACTIVO", resp.getNombre());

        EstadoResponse resp2 = new EstadoResponse();
        resp2.setId(2L);
        resp2.setNombre("INACTIVO");
        assertEquals(2L, resp2.getId());
        assertEquals("INACTIVO", resp2.getNombre());
    }

    @Test
    void rolRequestResponse_gettersSetters_yCtors() {
        RolRequest rr = new RolRequest("ADMIN");
        assertEquals("ADMIN", rr.getNombre());
        rr.setNombre("USER");
        assertEquals("USER", rr.getNombre());

        RolResponse resp = new RolResponse(1L, "ADMIN");
        assertEquals(1L, resp.getId());
        assertEquals("ADMIN", resp.getNombre());

        RolResponse resp2 = new RolResponse();
        resp2.setId(2L);
        resp2.setNombre("INVITADO");
        assertEquals(2L, resp2.getId());
        assertEquals("INVITADO", resp2.getNombre());
    }

    @Test
    void preguntaSeguridadDto_record_funciona() {
        PreguntaSeguridadDto dto = new PreguntaSeguridadDto(1, "¿Pregunta?");
        assertEquals(1, dto.id());
        assertEquals("¿Pregunta?", dto.texto());
    }

    @Test
    void setupSecurityQuestionsRequest_gettersSetters_funciona() {
        SetupSecurityQuestionsRequest req = new SetupSecurityQuestionsRequest();
        req.setUserId(3L);

        SetupSecurityQuestionsRequest.Item item1 = new SetupSecurityQuestionsRequest.Item();
        item1.setPreguntaId(1);
        item1.setRespuesta("perro");

        SetupSecurityQuestionsRequest.Item item2 = new SetupSecurityQuestionsRequest.Item();
        item2.setPreguntaId(3);
        item2.setRespuesta("pizza");

        SetupSecurityQuestionsRequest.Item item3 = new SetupSecurityQuestionsRequest.Item();
        item3.setPreguntaId(5);
        item3.setRespuesta("colegio");

        req.setItems(java.util.List.of(item1, item2, item3));

        assertEquals(3L, req.getUserId());
        assertEquals(3, req.getItems().size());
        assertEquals(1, req.getItems().get(0).getPreguntaId());
        assertEquals("perro", req.getItems().get(0).getRespuesta());
    }

}
