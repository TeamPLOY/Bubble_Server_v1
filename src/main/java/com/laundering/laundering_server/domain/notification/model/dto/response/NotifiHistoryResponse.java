package com.laundering.laundering_server.domain.notification.model.dto.response;

import java.time.LocalDate;

public record NotifiHistoryResponse(
        String name,
        String machine,

        LocalDate date
) {
}
