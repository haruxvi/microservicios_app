package com.microservice.auth_service.microservice_auth_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.auth_service.microservice_auth_service.controller.RolController;
import com.microservice.auth_service.microservice_auth_service.dto.RolRequest;
import com.microservice.auth_service.microservice_auth_service.dto.RolResponse;
import com.microservice.auth_service.microservice_auth_service.service.RolService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(controllers = RolController.class)
@AutoConfigureMockMvc(addFilters = false)
class RolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RolService rolService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll_debeRetornarListaDeRoles() throws Exception {
        RolResponse resp = new RolResponse(1L, "ADMIN");
        when(rolService.getAll()).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/auth/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("ADMIN"));
    }

    @Test
    void create_debeRetornarRolCreado() throws Exception {
        RolRequest request = new RolRequest("USER");
        RolResponse resp = new RolResponse(2L, "USER");

        when(rolService.create(any(RolRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/api/auth/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.nombre").value("USER"));
    }
}
