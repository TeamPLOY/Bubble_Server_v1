package com.ploy.bubble_server_v1.domain.reservation.model.dto.response;

import java.time.LocalDate;

public record ReservationSummaryResponse(
        LocalDate date, // 날짜를 LocalDate로 사용 (시간 제외)
        String day, // 무슨 요일인지
        Long userCount // 예약한 유저 수
) {
}
