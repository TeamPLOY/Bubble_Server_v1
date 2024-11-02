package com.ploy.bubble_server_v1.domain.reservation.controller;


import com.ploy.bubble_server_v1.domain.facade.ReservationFacade;
import com.ploy.bubble_server_v1.domain.reservation.model.dto.request.ReservationRequest;
import com.ploy.bubble_server_v1.domain.reservation.model.dto.response.ReservationSummaryResponse;
import com.ploy.bubble_server_v1.common.util.AuthenticationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "세탁기")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationFacade reservationFacade;


    @Operation(summary = "세탁기 예약")
    @PostMapping
    public ResponseEntity<Void> reservation(
            @RequestBody ReservationRequest reservationRequest
    ) {
        reservationFacade.reservation(AuthenticationUtil.getMemberId(),reservationRequest.date());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "세탁기 예약 취소")
    @PutMapping
    public ResponseEntity<Void> cancelReservation(
        @RequestBody ReservationRequest reservationRequest
    ) {
        reservationFacade.cancelReservation(AuthenticationUtil.getMemberId(),reservationRequest.date());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "세탁기 예약 상태 조회")
    @GetMapping
    public ResponseEntity<List<ReservationSummaryResponse>> getReservation() {
        List<ReservationSummaryResponse> reservations = reservationFacade.getReservation(AuthenticationUtil.getMemberId());
        return ResponseEntity.ok(reservations);
    }

    @Operation(summary = "당주 예약 여부 확인")
    @GetMapping("/isReserved")
    public ResponseEntity<Boolean> getIsReserved() {
        Boolean IsReserved = reservationFacade.getIsReserved(AuthenticationUtil.getMemberId());
        return ResponseEntity.ok(IsReserved);
    }
}
