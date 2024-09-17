package com.laundering.laundering_server.domain.washingMachine.controller;


import com.laundering.laundering_server.domain.facade.WashingMachineFacade;
import com.laundering.laundering_server.domain.washingMachine.model.dto.response.WashingMachineResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.laundering.laundering_server.common.util.AuthenticationUtil.getMemberId;

@Tag(name = "세탁기")
@Slf4j
@RestController
@RequiredArgsConstructor
public class WashingMachineController {
    private final WashingMachineFacade washingMachineFacade;

    @Operation(summary = "세탁기 시간 조회")
    @GetMapping("/washing")
    public ResponseEntity<List<WashingMachineResponse>> getStatus() {
        List<WashingMachineResponse> ws = washingMachineFacade.getStatus(getMemberId());
        return ResponseEntity.ok(ws);
    }

    @Operation(summary = "세탁기 예약")
    @PostMapping("/reservation")
    public ResponseEntity<Void> reservation() {
        washingMachineFacade.reservation(getMemberId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "세탁기 예약 취소")
    @PostMapping("/reservation/cancel")
    public ResponseEntity<Void> cancelReservation() {
        washingMachineFacade.cancelReservation(getMemberId());
        return ResponseEntity.noContent().build();
    }
}
