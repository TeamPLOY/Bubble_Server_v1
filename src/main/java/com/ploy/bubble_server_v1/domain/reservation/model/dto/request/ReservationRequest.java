package com.ploy.bubble_server_v1.domain.reservation.model.dto.request;

import java.time.LocalDate;

public record ReservationRequest(
        LocalDate date
) {
}
