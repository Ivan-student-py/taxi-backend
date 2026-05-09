package com.taxi.shared.exception;

import com.taxi.security.JwtUtils;
import com.taxi.trip.controller.StatsController;
import com.taxi.trip.service.StatsService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {StatsController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StatsService statsService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @Test
    void handleNotFound_ShouldReturn404() throws Exception {
        when(statsService.getTripStats()).thenThrow(new EntityNotFoundException("Passenger not found"));

        mockMvc.perform(get("/stats/trips"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Passenger not found"));
    }

    @Test
    void handleIllegalState_ShouldReturn409() throws Exception {
        when(statsService.getTripStats()).thenThrow(new IllegalStateException("Passenger already has an active trip"));

        mockMvc.perform(get("/stats/trips"))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Passenger already has an active trip"));
    }
}