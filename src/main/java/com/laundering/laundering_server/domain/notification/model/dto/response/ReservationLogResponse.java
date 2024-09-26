package com.laundering.laundering_server.domain.notification.model.dto.response;

import java.time.LocalDate;

public record ReservationLogResponse(
        LocalDate date,
        String resDate,
        boolean cancel,
        String washingRoom
) {
}
