package com.laundering.laundering_server.domain.facade;


import com.laundering.laundering_server.common.exception.BusinessException;
import com.laundering.laundering_server.common.exception.ErrorCode;
import com.laundering.laundering_server.domain.member.model.dto.request.*;
import com.laundering.laundering_server.domain.member.model.dto.response.LoginResponse;
import com.laundering.laundering_server.domain.member.model.dto.response.TokenResponse;
import com.laundering.laundering_server.domain.member.model.dto.response.UserResponse;
import com.laundering.laundering_server.domain.member.service.AuthService;
import com.laundering.laundering_server.domain.member.service.*;
import com.laundering.laundering_server.domain.member.service.UserService;
import com.laundering.laundering_server.domain.washingMachine.controller.WashingMachineController;
import com.laundering.laundering_server.domain.washingMachine.model.dto.response.WashingMachineResponse;
import com.laundering.laundering_server.domain.washingMachine.service.WashingMachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WashingMachineFacade {
    private final WashingMachineService washingMachineService; // AuthService 의존성 주입
    @Transactional
    public List<WashingMachineResponse> getStatus(Long id) {
        return washingMachineService.getStatus(id);
    }

}

