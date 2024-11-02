package com.ploy.bubble_server_v1.domain.facade;

import com.ploy.bubble_server_v1.domain.reservation.model.dto.response.WashingMachineResponse;
import com.ploy.bubble_server_v1.domain.washingMachine.service.WashingMachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Component
@RequiredArgsConstructor
public class WashingMachineFacade {
    private final WashingMachineService washingMachineService;

    @Transactional
    public List<WashingMachineResponse> getStatus(Long id) {
        return washingMachineService.getStatus(id);
    }
}
