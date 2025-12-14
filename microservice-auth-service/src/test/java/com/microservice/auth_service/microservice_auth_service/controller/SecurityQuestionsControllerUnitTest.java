package com.microservice.auth_service.microservice_auth_service.controller;

import com.microservice.auth_service.microservice_auth_service.dto.SecurityQuestionDto;
import com.microservice.auth_service.microservice_auth_service.dto.SetupSecurityQuestionsRequest;
import com.microservice.auth_service.microservice_auth_service.service.SecurityQuestionsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SecurityQuestionsControllerUnitTest {

    private MockMvc mvc;
    private SecurityQuestionsService service;
    private ObjectMapper om;

    @BeforeEach
    void setup() {
        service = Mockito.mock(SecurityQuestionsService.class);
        om = new ObjectMapper();
        SecurityQuestionsController controller = new SecurityQuestionsController(service);
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void list_ok_200() throws Exception {
        when(service.listarActivas()).thenReturn(List.of(
                new SecurityQuestionDto(1, "P1"),
                new SecurityQuestionDto(2, "P2")
        ));

        mvc.perform(get("/api/auth/security-questions"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1));

        verify(service).listarActivas();
    }

    @Test
    void setup_ok_204() throws Exception {
        SetupSecurityQuestionsRequest req = new SetupSecurityQuestionsRequest();
        req.setUserId(1L);
        SetupSecurityQuestionsRequest.Item i1 = new SetupSecurityQuestionsRequest.Item();
        i1.setPreguntaId(1); i1.setRespuesta("a");
        SetupSecurityQuestionsRequest.Item i2 = new SetupSecurityQuestionsRequest.Item();
        i2.setPreguntaId(2); i2.setRespuesta("b");
        SetupSecurityQuestionsRequest.Item i3 = new SetupSecurityQuestionsRequest.Item();
        i3.setPreguntaId(3); i3.setRespuesta("c");
        req.setItems(List.of(i1,i2,i3));

        doNothing().when(service).setupPreguntas(any());

        mvc.perform(post("/api/auth/security-questions/setup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isNoContent());

        verify(service).setupPreguntas(any(SetupSecurityQuestionsRequest.class));
    }

    @Test
    void setup_badRequest_mapea400() throws Exception {
        SetupSecurityQuestionsRequest req = new SetupSecurityQuestionsRequest();
        req.setUserId(null);
        req.setItems(List.of());

        doThrow(new IllegalArgumentException("userId es requerido"))
                .when(service).setupPreguntas(any());

        var res = mvc.perform(post("/api/auth/security-questions/setup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println("BODY => " + res.getResponse().getContentAsString());
        System.out.println("CONTENT-TYPE => " + res.getResponse().getContentType());
    }

}
