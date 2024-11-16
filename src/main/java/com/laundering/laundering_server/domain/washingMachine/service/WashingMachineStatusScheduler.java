package com.laundering.laundering_server.domain.washingMachine.service;

import com.laundering.laundering_server.common.firebase.FirebaseService;
import com.laundering.laundering_server.domain.member.model.entity.Users;
import com.laundering.laundering_server.domain.member.repository.UsersRepository;
import com.laundering.laundering_server.domain.notification.model.entity.NotifiReservation;
import com.laundering.laundering_server.domain.notification.repository.NotifiReservationRepository;
import com.laundering.laundering_server.domain.washingMachine.model.dto.response.WashingMachineResponse;
import com.laundering.laundering_server.domain.washingMachine.model.entity.Reservation;
import com.laundering.laundering_server.domain.washingMachine.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Component
@RequiredArgsConstructor
public class WashingMachineStatusScheduler {

    private final WashingMachineStatusService statusService;
    private final NotifiReservationRepository notifiReservationRepository;
    private final ReservationRepository reservationRepository;
    private final FirebaseService firebaseService;
    private final UsersRepository usersRepository;

    @Scheduled(fixedRate = 60000)
    public void updateWashingMachineStatus() {
        log.info("모든 사용자의 세탁기 및 건조기 상태를 업데이트하는 중...");

        // 1. 세탁기 및 건조기 상태 가져오기
        List<WashingMachineResponse> statusList = statusService.getStatus();

        // 2. Redis에 상태 저장
        statusService.saveAllStatusToRedis(statusList);
        log.info("상태 업데이트가 완료되었습니다.");

        // 3. 남은 시간이 0인 세탁기와 건조기 필터링
        List<WashingMachineResponse> machinesAboutToFinish = statusList.stream()
                .filter(machine -> machine.time() == 0.0)
                .collect(Collectors.toList());

        // 4. 각 상태별 알림 처리
        for (WashingMachineResponse machine : machinesAboutToFinish) {
            String machineName = machine.name();

            if (machineName.contains("세탁기")) {
                // 세탁기 처리
                List<Reservation> reservations = reservationRepository.findByDateAndMachineAndCancelFalse(LocalDate.now(), machineName);
                for (Reservation reservation : reservations) {
                    List<NotifiReservation> notifiReservations = notifiReservationRepository.findByUserId(reservation.getUserId());
                    for (NotifiReservation notifi : notifiReservations) {
                        firebaseService.sendFcmNotification(notifi.getToken(), machineName);
                    }
                }
            } else if (machineName.contains("건조기")) {
                // 건조기 처리
                List<Reservation> reservations = reservationRepository.findByDateAndCancelFalse(LocalDate.now());
                for (Reservation reservation : reservations) {
                    // Users 테이블을 통해 세탁실 정보를 가져옴
                    Users user = usersRepository.findWashingRoomById(reservation.getUserId());

                    // 건조기 이름의 앞 3자리와 세탁실 정보가 일치하는지 확인
                    if (machineName.substring(0, 3).equals(user.getWashingRoom())) {
                        // 사용자별 알림 예약 정보 조회
                        List<NotifiReservation> notifiReservations = notifiReservationRepository.findByUserId(reservation.getUserId());

                        // FCM 알림 전송
                        for (NotifiReservation notifi : notifiReservations) {
                            firebaseService.sendFcmNotification(notifi.getToken(), machineName);
                        }
                    }
                }
            }
        }
    }
}
