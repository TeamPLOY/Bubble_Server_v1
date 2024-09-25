package com.laundering.laundering_server.domain.washingMachine.model.dto.request;

import java.time.LocalDateTime;

public record ReservationRequest(
        LocalDateTime date
) {
}
