package com.taxi.notification.controller;

import com.taxi.notification.dto.NotificationCreateRequest;
import com.taxi.notification.entity.NotificationTask;
import com.taxi.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationTask> createNotification(@RequestBody NotificationCreateRequest req) {
        return ResponseEntity.ok(notificationService.createNotification(req));
    }

    @GetMapping
    public ResponseEntity<List<NotificationTask>> getNotifications(@RequestParam(required = false) Long trip_id) {
        if (trip_id != null) {
            return ResponseEntity.ok(notificationService.findByTripId(trip_id));
        }
        return ResponseEntity.ok(notificationService.findAll());
    }
}