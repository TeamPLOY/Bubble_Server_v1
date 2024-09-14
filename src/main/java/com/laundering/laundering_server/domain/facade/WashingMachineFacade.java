package com.laundering.laundering_server.domain.facade;

import com.laundering.laundering_server.domain.washingMachine.model.dto.response.WashingMachineResponse;
import com.laundering.laundering_server.domain.washingMachine.service.ReservationService;
import com.laundering.laundering_server.domain.washingMachine.service.WashingMachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WashingMachineFacade {
    private final WashingMachineService washingMachineService;
    private final ReservationService reservationService;
    @Transactional
    public List<WashingMachineResponse> getStatus(Long id) {
        return washingMachineService.getStatus(id);
    }

    @Transactional
    public void reservation(Long id) {
        reservationService.reservation(id);
    }


}

