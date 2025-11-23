package com.microservice.quiz_service.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DtoTest {

    @Test
    void categoriaRequest_gettersSetters() {
        CategoriaRequest dto = new CategoriaRequest();
        dto.setNombre("Matemáticas");
        assertEquals("Matemáticas", dto.getNombre());

        CategoriaRequest dto2 = new CategoriaRequest("Historia");
        assertEquals("Historia", dto2.getNombre());
    }

    @Test
    void dificultadRequest_gettersSetters() {
        DificultadRequest dto = new DificultadRequest();
        dto.setNombre("Fácil");
        assertEquals("Fácil", dto.getNombre());

        DificultadRequest dto2 = new DificultadRequest("Difícil");
        assertEquals("Difícil", dto2.getNombre());
    }

    @Test
    void estadoRequest_gettersSetters() {
        EstadoRequest dto = new EstadoRequest();
        dto.setNombre("ACTIVO");
        assertEquals("ACTIVO", dto.getNombre());

        EstadoRequest dto2 = new EstadoRequest("INACTIVO");
        assertEquals("INACTIVO", dto2.getNombre());
    }

    @Test
    void opcionRequest_gettersSetters() {
        OpcionRequest dto = new OpcionRequest();
        dto.setTexto("4");
        dto.setEsCorrecta(true);

        assertEquals("4", dto.getTexto());
        assertTrue(dto.isEsCorrecta());

        OpcionRequest dto2 = new OpcionRequest("3", false);
        assertEquals("3", dto2.getTexto());
        assertFalse(dto2.isEsCorrecta());
    }

    @Test
    void categoriaResponse_gettersSetters() {
        CategoriaResponse dto = new CategoriaResponse();
        dto.setId(1L);
        dto.setNombre("Matemáticas");

        assertEquals(1L, dto.getId());
        assertEquals("Matemáticas", dto.getNombre());
    }

    @Test
    void dificultadResponse_gettersSetters() {
        DificultadResponse dto = new DificultadResponse();
        dto.setId(2L);
        dto.setNombre("Fácil");

        assertEquals(2L, dto.getId());
        assertEquals("Fácil", dto.getNombre());

        DificultadResponse dto2 = new DificultadResponse(3L, "Media");
        assertEquals(3L, dto2.getId());
        assertEquals("Media", dto2.getNombre());
    }

    @Test
    void estadoResponse_gettersSetters() {
        EstadoResponse dto = new EstadoResponse();
        dto.setId(4L);
        dto.setNombre("ACTIVO");

        assertEquals(4L, dto.getId());
        assertEquals("ACTIVO", dto.getNombre());

        EstadoResponse dto2 = new EstadoResponse(5L, "INACTIVO");
        assertEquals(5L, dto2.getId());
        assertEquals("INACTIVO", dto2.getNombre());
    }

    @Test
    void opcionResponse_gettersSetters() {
        OpcionResponse dto = new OpcionResponse();
        dto.setId(6L);
        dto.setTexto("Respuesta");
        dto.setEsCorrecta(true);

        assertEquals(6L, dto.getId());
        assertEquals("Respuesta", dto.getTexto());
        assertTrue(dto.isEsCorrecta());

        OpcionResponse dto2 = new OpcionResponse(7L, "Otra", false);
        assertEquals(7L, dto2.getId());
        assertEquals("Otra", dto2.getTexto());
        assertFalse(dto2.isEsCorrecta());
    }

    @Test
    void preguntaRequest_gettersSetters() {
        PreguntaRequest dto = new PreguntaRequest();
        dto.setEnunciado("¿Capital de Francia?");
        dto.setIdCategoria(1L);
        dto.setIdDificultad(2L);
        dto.setIdEstado(3L);
        dto.setOpciones(List.of(new OpcionRequest("París", true)));

        assertEquals("¿Capital de Francia?", dto.getEnunciado());
        assertEquals(1L, dto.getIdCategoria());
        assertEquals(2L, dto.getIdDificultad());
        assertEquals(3L, dto.getIdEstado());
        assertEquals(1, dto.getOpciones().size());
    }

    @Test
    void preguntaResponse_gettersSetters() {
        OpcionResponse op = new OpcionResponse(1L, "París", true);
        PreguntaResponse dto = new PreguntaResponse(
                10L,
                "¿Capital de Francia?",
                "Geografía",
                "Fácil",
                "ACTIVA",
                List.of(op)
        );

        assertEquals(10L, dto.getId());
        assertEquals("¿Capital de Francia?", dto.getEnunciado());
        assertEquals("Geografía", dto.getCategoria());
        assertEquals("Fácil", dto.getDificultad());
        assertEquals("ACTIVA", dto.getEstado());
        assertEquals(1, dto.getOpciones().size());

        dto.setId(11L);
        dto.setEnunciado("¿2+2?");
        dto.setCategoria("Matemáticas");
        dto.setDificultad("Media");
        dto.setEstado("INACTIVA");
        dto.setOpciones(List.of(op));

        assertEquals(11L, dto.getId());
        assertEquals("¿2+2?", dto.getEnunciado());
        assertEquals("Matemáticas", dto.getCategoria());
        assertEquals("Media", dto.getDificultad());
        assertEquals("INACTIVA", dto.getEstado());
        assertEquals(1, dto.getOpciones().size());
    }
}
