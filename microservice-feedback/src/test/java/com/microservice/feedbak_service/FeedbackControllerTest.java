
import com.example.quizapp.feedback.dto.FeedbackRequest;
import com.example.quizapp.feedback.dto.FeedbackResponse;
import com.example.quizapp.feedback.service.FeedbackService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FeedbackController.class)
class FeedbackControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private FeedbackService service;
    @Autowired private ObjectMapper mapper;

    private final FeedbackRequest request = new FeedbackRequest(1L, "La app se cierra", "BUG", "APP");
    private final FeedbackResponse response = new FeedbackResponse(1L, "La app se cierra", "BUG", "APP",
            "15/06/2025 14:30", false, 1L);

    @Test
    void crearFeedback_DebeRetornar201YFeedback() throws Exception {
        when(service.crear(any())).thenReturn(response);

        mockMvc.perform(post("/api/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("La app se cierra"))
                .andExpect(jsonPath("$.tipo").value("BUG"));
    }

    @Test
    void obtenerTodos_DebeRetornarLista() throws Exception {
        when(service.todos()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/feedback"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void obtenerPendientes_DebeRetornarLista() throws Exception {
        when(service.pendientes()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/feedback/pendientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].resuelto").value(false));
    }

    @Test
    void resolverFeedback_DebeMarcarComoResuelto() throws Exception {
        FeedbackResponse resuelto = new FeedbackResponse(1L, "La app se cierra", "BUG", "APP",
                "15/06/2025 14:30", true, 1L);
        when(service.resolver(1L)).thenReturn(resuelto);

        mockMvc.perform(patch("/api/feedback/1/resolver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resuelto").value(true));
    }
}