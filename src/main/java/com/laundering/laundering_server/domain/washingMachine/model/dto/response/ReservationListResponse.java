package com.laundering.laundering_server.domain.washingMachine.model.dto.response;

import java.util.List;

public record ReservationListResponse(
        String date,
        String day,
        List<String>userCount
) {
}
