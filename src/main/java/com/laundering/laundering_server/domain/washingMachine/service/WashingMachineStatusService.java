package com.laundering.laundering_server.domain.washingMachine.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laundering.laundering_server.common.exception.BusinessException;
import com.laundering.laundering_server.common.exception.ErrorCode;
import com.laundering.laundering_server.domain.washingMachine.model.dto.response.WashingMachineResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class WashingMachineStatusService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String REDIS_KEY_PREFIX = "washingMachineStatus:";

    public List<WashingMachineResponse> getStatus() {
        try {
            String url = "https://build-bubble-proxy-server.vercel.app/home";
            String response = restTemplate.getForObject(url, String.class);
            List<WashingMachineResponse> statusList = objectMapper.readValue(response, new TypeReference<List<WashingMachineResponse>>() {});
            saveAllStatusToRedis(statusList);
            return statusList;
        } catch (IOException e) {
            log.error("세탁기 API 응답 파싱에 실패했습니다: {}", e.getMessage());
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }
    }

    public List<WashingMachineResponse> getAllStatusFromRedis() {
        String redisKey = REDIS_KEY_PREFIX + "all";
        String jsonStatusList = redisTemplate.opsForValue().get(redisKey);
        if (jsonStatusList != null) {
            try {
                return objectMapper.readValue(jsonStatusList, new TypeReference<List<WashingMachineResponse>>() {});
            } catch (IOException e) {
                log.error("Redis에서 JSON 파싱에 실패했습니다: {}", e.getMessage());
            }
        }
        return null;
    }

    public void saveAllStatusToRedis(List<WashingMachineResponse> statusList) {
        String redisKey = REDIS_KEY_PREFIX + "all";
        try {
            String jsonStatusList = objectMapper.writeValueAsString(statusList);
            redisTemplate.opsForValue().set(redisKey, jsonStatusList);
            log.info("전체 세탁기 상태가 Redis에 저장되었습니다: 키={}, 값={}", redisKey, jsonStatusList);
        } catch (IOException e) {
            log.error("상태 리스트를 JSON으로 변환하는 데 실패했습니다: {}", e.getMessage());
        }
    }

    public List<WashingMachineResponse> getStatusFromRedis(String washingRoom) {
        String redisKey = REDIS_KEY_PREFIX + "all";
        String jsonStatusList = redisTemplate.opsForValue().get(redisKey);

        if (jsonStatusList != null) {
            try {
                // 전체 리스트를 파싱
                List<WashingMachineResponse> statusList = objectMapper.readValue(jsonStatusList, new TypeReference<List<WashingMachineResponse>>() {});

                // washingRoom 값과 일치하는 name 필터링
                List<WashingMachineResponse> filteredList = statusList.stream()
                        .filter(status -> status.name().startsWith(washingRoom + " "))
                        .collect(Collectors.toList());

                return filteredList;
            } catch (IOException e) {
                log.error("Redis에서 JSON 파싱에 실패했습니다: {}", e.getMessage());
            }
        }
        return null;
    }

}