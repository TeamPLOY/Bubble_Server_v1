package com.laundering.laundering_server.domain.washingMachine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WashingMachineStatusScheduler {

    private final WashingMachineStatusService statusService;

    @Scheduled(fixedRate = 60000)
    public void updateWashingMachineStatus() {
        log.info("모든 사용자의 세탁기 상태를 업데이트하는 중...");
        var statusList = statusService.getStatus(); // 전체 상태 가져오기
        statusService.saveAllStatusToRedis(statusList); // 전체 상태 Redis에 저장
        log.info("전체 세탁기 상태 업데이트가 완료되었습니다.");
    }
}