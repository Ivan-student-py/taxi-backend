package com.taxi.trip.controller;

import com.taxi.security.JwtUtils;
import com.taxi.trip.dto.TripStatsResponse;
import com.taxi.trip.service.StatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatsController.class)
@AutoConfigureMockMvc(addFilters = false)
class StatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StatsService statsService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @Test
    void getTripStats_ShouldReturn200WithJson() throws Exception {
        when(statsService.getTripStats())
                .thenReturn(new TripStatsResponse(4L, 1L, new BigDecimal("525.00"), new BigDecimal("2100.00")));

        mockMvc.perform(get("/stats/trips"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalTrips").value(4))
                .andExpect(jsonPath("$.averagePrice").value(525.00));
    }
}