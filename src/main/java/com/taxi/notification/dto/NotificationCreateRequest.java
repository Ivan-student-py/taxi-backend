package com.taxi.notification.dto;

public record NotificationCreateRequest(
        Long tripId,
        String recipientType,
        Long recipientId,
        String message
) {}