package com.laundering.laundering_server.domain.washingMachine.model.dto.request;

import java.time.LocalDate;

public record ReservationRequest(
        LocalDate date,
        String machine
) {
}
