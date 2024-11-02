package com.ploy.bubble_server_v1.domain.washingMachine.controller;


import com.ploy.bubble_server_v1.domain.facade.WashingMachineFacade;
import com.ploy.bubble_server_v1.domain.reservation.model.dto.response.WashingMachineResponse;
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
@RequestMapping("/api")
public class WashingMachineController {
    private final WashingMachineFacade washingMachineFacade;

    @Operation(summary = "세탁기 시간 조회")
    @GetMapping("/washing")
    public ResponseEntity<List<WashingMachineResponse>> getStatus() {
        List<WashingMachineResponse> ws = washingMachineFacade.getStatus(AuthenticationUtil.getMemberId());
        return ResponseEntity.ok(ws);
    }
}
