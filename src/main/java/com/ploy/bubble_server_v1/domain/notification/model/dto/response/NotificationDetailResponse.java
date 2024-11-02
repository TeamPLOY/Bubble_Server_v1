package com.ploy.bubble_server_v1.domain.notification.model.dto.response;

import java.time.LocalDate;

public record NotificationDetailResponse(
        String title,
        String detail,
        LocalDate date
) {
}
