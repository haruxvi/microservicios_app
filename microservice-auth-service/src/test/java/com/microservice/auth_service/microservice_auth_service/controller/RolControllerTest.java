package com.microservice.auth_service.microservice_auth_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.auth_service.microservice_auth_service.dto.RolRequest;
import com.microservice.auth_service.microservice_auth_service.dto.RolResponse;
import com.microservice.auth_service.microservice_auth_service.service.RolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RolControllerTest {

    private MockMvc mockMvc;
    private RolService rolService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        rolService = Mockito.mock(RolService.class);
        objectMapper = new ObjectMapper();

        RolController controller = new RolController(rolService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("USER"));
    }
}
