package com.laundering.laundering_server.domain.washingMachine.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laundering.laundering_server.common.exception.BusinessException;
import com.laundering.laundering_server.common.exception.ErrorCode;
import com.laundering.laundering_server.common.jwt.Jwt;
import com.laundering.laundering_server.domain.member.model.dto.request.TokenRefreshRequest;
import com.laundering.laundering_server.domain.member.model.dto.response.LoginResponse;
import com.laundering.laundering_server.domain.member.model.dto.response.TokenResponse;
import com.laundering.laundering_server.domain.member.model.entity.Email;
import com.laundering.laundering_server.domain.member.repository.UserRepository;
import com.laundering.laundering_server.domain.member.model.entity.User;
import com.laundering.laundering_server.domain.washingMachine.model.dto.response.WashingMachineResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class WashingMachineService
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public List<WashingMachineResponse> getStatus(Long id) {
        try {
            // 주어진 id로 사용자 정보를 조회
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));

            // 세탁실 정보 가져오기
            String washingRoom = user.getWashingRoom();

            // URL을 구성하고 HTTP GET 요청 보내기
            String url = String.format("https://build-bubble-proxy-server.vercel.app/home/%s", washingRoom);
            String response = restTemplate.getForObject(url, String.class);

            // JSON 응답을 WashingMachineResponse 리스트로 변환하기
            return objectMapper.readValue(response, new TypeReference<List<WashingMachineResponse>>() {});
        } catch (IOException e) {
            // JSON 처리 중 오류가 발생한 경우 또는 IO 오류가 발생한 경우 처리
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }
    }
}


