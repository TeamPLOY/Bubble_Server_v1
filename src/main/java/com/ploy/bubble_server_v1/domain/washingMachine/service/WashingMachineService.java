package com.ploy.bubble_server_v1.domain.washingMachine.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ploy.bubble_server_v1.common.exception.BusinessException;
import com.ploy.bubble_server_v1.common.exception.ErrorCode;
import com.ploy.bubble_server_v1.domain.reservation.model.dto.response.WashingMachineResponse;
import com.ploy.bubble_server_v1.domain.user.model.entity.User;
import com.ploy.bubble_server_v1.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;


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


