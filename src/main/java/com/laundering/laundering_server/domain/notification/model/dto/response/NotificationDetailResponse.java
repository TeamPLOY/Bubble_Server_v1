package com.laundering.laundering_server.domain.notification.model.dto.response;

import java.time.LocalDate;

public record NotificationDetailResponse(
        String title,
        String detail,
        LocalDate date
) {
}
