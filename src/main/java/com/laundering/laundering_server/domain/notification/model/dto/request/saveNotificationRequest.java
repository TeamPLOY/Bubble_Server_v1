package com.laundering.laundering_server.domain.notification.model.dto.request;

public record saveNotificationRequest(
        String token,
        String machine
) {
}