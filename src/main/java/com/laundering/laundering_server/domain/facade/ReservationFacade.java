package com.laundering.laundering_server.domain.facade;

import com.laundering.laundering_server.domain.member.model.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReservationFacade {
    @Transactional
    public void reservation(String accessToken) {

    }
}