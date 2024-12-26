package com.laundering.laundering_server.domain.facade;

import com.laundering.laundering_server.domain.member.service.UserService;
import com.laundering.laundering_server.domain.washingMachine.model.dto.request.ReservationRequest;
import com.laundering.laundering_server.domain.washingMachine.model.dto.response.ReservationSummaryResponse;
import com.laundering.laundering_server.domain.washingMachine.model.dto.response.WashingMachineResponse;
import com.laundering.laundering_server.domain.washingMachine.service.ReservationService;
import com.laundering.laundering_server.domain.washingMachine.service.WashingMachineStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WashingMachineFacade {
    private final WashingMachineStatusService washingMachineStatusService;
    private final ReservationService reservationService;
    private final UserService userService;
    @Transactional
    public List<WashingMachineResponse> getStatus(Long id) {
        String washingroom = userService.getwashingRoom(id);
        return washingMachineStatusService.getStatusFromRedis(washingroom);
    }

    @Transactional
    public void reservation(Long id, ReservationRequest req) {
        reservationService.reservation(id,req.date(),req.machine());
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

