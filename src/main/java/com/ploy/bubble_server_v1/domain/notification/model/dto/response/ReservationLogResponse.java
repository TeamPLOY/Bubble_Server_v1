package com.ploy.bubble_server_v1.domain.notification.model.dto.response;

import java.time.LocalDate;

public record ReservationLogResponse(
        LocalDate date,
        boolean cancel,
        String washingRoom
) {
}
