package com.taxi.notification.controller;

import com.taxi.notification.dto.NotificationCreateRequest;
import com.taxi.notification.entity.NotificationStatus;
import com.taxi.notification.entity.NotificationTask;
import com.taxi.notification.service.NotificationService;
import com.taxi.security.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService notificationService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @Test
    void getNotifications_WithTripId_ShouldReturn200() throws Exception {
        NotificationTask task = NotificationTask.builder()
                .id(1L).tripId(1L).recipientType("PASSENGER")
                .recipientId(1L).message("Test").status(NotificationStatus.SENT).attempts(0).build();

        when(notificationService.findByTripId(1L)).thenReturn(List.of(task));

        mockMvc.perform(get("/notifications").param("trip_id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tripId").value(1))
                .andExpect(jsonPath("$[0].status").value("SENT"));
    }

    @Test
    void createNotification_ShouldReturn200() throws Exception {
        NotificationCreateRequest req = new NotificationCreateRequest(1L, "PASSENGER", 1L, "Test msg");
        NotificationTask saved = NotificationTask.builder().id(2L).status(NotificationStatus.PENDING).build();

        when(notificationService.createNotification(req)).thenReturn(saved);

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tripId\":1,\"recipientType\":\"PASSENGER\",\"recipientId\":1,\"message\":\"Test msg\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }
}