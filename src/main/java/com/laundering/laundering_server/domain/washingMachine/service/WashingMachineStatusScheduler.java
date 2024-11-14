package com.laundering.laundering_server.domain.washingMachine.service;

import com.laundering.laundering_server.common.firebase.FirebaseService;
import com.laundering.laundering_server.domain.notification.model.entity.NotifiReservation;
import com.laundering.laundering_server.domain.notification.repository.NotifiReservationRepository;
import com.laundering.laundering_server.domain.notification.service.NotificationService;
import com.laundering.laundering_server.domain.washingMachine.model.dto.response.WashingMachineResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class WashingMachineStatusScheduler {

    private final WashingMachineStatusService statusService;
    private final NotifiReservationRepository notifiReservationRepository;
    private final FirebaseService firebaseService;


    @Scheduled(fixedRate = 60000)
    public void updateWashingMachineStatus() {
        log.info("모든 사용자의 세탁기 상태를 업데이트하는 중...");

        // 1. 세탁기 전체 상태 가져오기
        List<WashingMachineResponse> statusList = statusService.getStatus();

        // 2. Redis에 전체 상태 저장
        statusService.saveAllStatusToRedis(statusList);
        log.info("전체 세탁기 상태 업데이트가 완료되었습니다.");

        // 3. 남은 시간이 1분인 세탁기 필터링
        List<WashingMachineResponse> machinesAboutToFinish = statusList.stream()
                .filter(machine -> machine.time() == 1.0)
                .collect(Collectors.toList());

        // 4. 예약된 알림 조회 및 FCM 알림 전송
        for (WashingMachineResponse machine : machinesAboutToFinish) {
            List<NotifiReservation> reservations = notifiReservationRepository.findByMachine(machine.name());
            for (NotifiReservation reservation : reservations) {
                // FCM 알림 전송
                firebaseService.sendFcmNotification(reservation.getToken(), machine.name());

                // 알림 전송 후 예약 삭제 (필요 시)
//                notifiReservationRepository.delete(reservation);
            }
        }
    }
}