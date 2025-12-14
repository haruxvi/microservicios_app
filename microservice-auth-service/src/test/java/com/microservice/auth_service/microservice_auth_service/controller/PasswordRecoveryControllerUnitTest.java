package com.microservice.auth_service.microservice_auth_service.controller;

import com.microservice.auth_service.microservice_auth_service.dto.RecoveryQuestionsResponse;
import com.microservice.auth_service.microservice_auth_service.dto.ResetPasswordRequest;
import com.microservice.auth_service.microservice_auth_service.dto.VerifyRecoveryRequest;
import com.microservice.auth_service.microservice_auth_service.dto.VerifyRecoveryResponse;
import com.microservice.auth_service.microservice_auth_service.dto.PreguntaSeguridadDto;
import com.microservice.auth_service.microservice_auth_service.service.PasswordRecoveryService;
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

class PasswordRecoveryControllerUnitTest {

    private MockMvc mvc;
    private PasswordRecoveryService service;
    private ObjectMapper om;

    @BeforeEach
    void setup() {
        service = Mockito.mock(PasswordRecoveryService.class);
        om = new ObjectMapper();
        PasswordRecoveryController controller = new PasswordRecoveryController(service);
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void questions_ok_200() throws Exception {
        RecoveryQuestionsResponse resp = new RecoveryQuestionsResponse(1L, List.of(
                new PreguntaSeguridadDto(10, "P10"),
                new PreguntaSeguridadDto(20, "P20"),
                new PreguntaSeguridadDto(30, "P30")
        ));
        when(service.getQuestions("a@a.com")).thenReturn(resp);

        mvc.perform(get("/api/auth/password-recovery/questions")
                        .param("identificador", "a@a.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.questions.length()").value(3));

        verify(service).getQuestions("a@a.com");
    }

    @Test
    void verify_ok_200_token() throws Exception {
        VerifyRecoveryRequest req = new VerifyRecoveryRequest(1L, List.of(
                new VerifyRecoveryRequest.Item(10, "a"),
                new VerifyRecoveryRequest.Item(20, "b"),
                new VerifyRecoveryRequest.Item(30, "c")
        ));

        when(service.verifyAnswers(any())).thenReturn("token123");

        mvc.perform(post("/api/auth/password-recovery/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("token123"));

        verify(service).verifyAnswers(any(VerifyRecoveryRequest.class));
    }

    @Test
    void reset_ok_204() throws Exception {
        ResetPasswordRequest req = new ResetPasswordRequest("t", "NuevaClave#2025");
        doNothing().when(service).resetPassword(any());

        mvc.perform(post("/api/auth/password-recovery/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isNoContent());

        verify(service).resetPassword(any(ResetPasswordRequest.class));
    }

    @Test
        void reset_badRequest_mapea400() throws Exception {
        ResetPasswordRequest req = new ResetPasswordRequest("", "123");

        mvc.perform(post("/api/auth/password-recovery/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
        }


}
