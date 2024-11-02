package com.ploy.bubble_server_v1.domain.facade;

import com.ploy.bubble_server_v1.domain.reservation.model.dto.response.ReservationSummaryResponse;
import com.ploy.bubble_server_v1.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationFacade {
    private final ReservationService reservationService;

    @Transactional
    public void reservation(Long id, LocalDate date) {
        reservationService.reservation(id,date);
    }

    @Transactional
    public void cancelReservation(Long id, LocalDate date) {
        reservationService.cancelReservation(id,date);
    }

    @Transactional
    public List<ReservationSummaryResponse> getReservation(Long id) {
        return reservationService.getReservation(id);
    }

    public boolean getIsReserved(Long id) {
        return reservationService.getIsReserved(id);
    }

}

