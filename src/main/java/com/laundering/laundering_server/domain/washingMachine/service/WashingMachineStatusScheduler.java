package com.laundering.laundering_server.domain.washingMachine.service;

import com.laundering.laundering_server.common.firebase.FirebaseService;
import com.laundering.laundering_server.domain.member.model.entity.Users;
import com.laundering.laundering_server.domain.member.repository.UsersRepository;
import com.laundering.laundering_server.domain.notification.model.entity.NotifiHistory;
import com.laundering.laundering_server.domain.notification.model.entity.NotifiReservation;
import com.laundering.laundering_server.domain.notification.repository.NotifiHistoryRepository;
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
@Component
@RequiredArgsConstructor
@Slf4j
public class WashingMachineStatusScheduler {

    private final WashingMachineStatusService statusService;
    private final NotifiReservationRepository notifiReservationRepository;
    private final ReservationRepository reservationRepository;
    private final FirebaseService firebaseService;
    private final UsersRepository usersRepository;
    private final NotifiHistoryRepository notifiHistoryRepository;

    // 평일: 20시 ~ 익일 1시 (매 1분마다 실행)
    @Scheduled(cron = "0 0-59 20-23 * * MON-FRI", zone = "Asia/Seoul")
    @Scheduled(cron = "0 0-59 0 * * TUE-SAT", zone = "Asia/Seoul")
    public void updateWashingMachineStatusWeekdays() {
        executeTask();
    }

    // 일요일: 18시 ~ 익일 1시 (매 1분마다 실행)
    @Scheduled(cron = "0 0-59 18-23 * * SUN", zone = "Asia/Seoul")
    @Scheduled(cron = "0 0-59 0 * * MON", zone = "Asia/Seoul")
    public void updateWashingMachineStatusSunday() {
        executeTask();
    }

    // 실행 로직
    private void executeTask() {
        log.info("모든 사용자의 세탁기 및 건조기 상태를 업데이트하는 중...");

        // 1. 세탁기 및 건조기 상태 가져오기
        List<WashingMachineResponse> statusList = statusService.getStatus();

        // 2. Redis에 상태 저장
        statusService.saveAllStatusToRedis(statusList);
        log.info("상태 업데이트가 완료되었습니다.");

        // 3. 남은 시간이 0인 세탁기와 건조기 필터링
        List<WashingMachineResponse> machinesAboutToFinish = statusList.stream()
                .filter(machine -> machine.time() == 1.0)
                .collect(Collectors.toList());

        // 4. 알림 처리
        machinesAboutToFinish.forEach(this::handleNotification);
    }

    private void handleNotification(WashingMachineResponse machine) {
        String machineName = machine.name();

        if (machineName.contains("세탁기")) {
            handleWashingMachineNotifications(machineName);
        } else if (machineName.contains("건조기")) {
            handleDryerNotifications(machineName);
        }
    }

    // 세탁기 알림 처리
    private void handleWashingMachineNotifications(String machineName) {
        List<Reservation> reservations = reservationRepository.findByDateAndMachineAndCancelFalse(LocalDate.now(), machineName);
        for (Reservation reservation : reservations) {
            List<NotifiReservation> notifiReservations = notifiReservationRepository.findByUserId(reservation.getUserId());
            for (NotifiReservation notifi : notifiReservations) {
                saveNotificationHistory(reservation.getUserId(), machineName);
                firebaseService.sendFcmNotification(notifi.getToken(), machineName);
            }
        }
    }

    // 건조기 알림 처리
    private void handleDryerNotifications(String machineName) {
        List<Reservation> reservations = reservationRepository.findByDateAndCancelFalse(LocalDate.now());
        for (Reservation reservation : reservations) {
            Users user = usersRepository.findWashingRoomById(reservation.getUserId());
            if (machineName.substring(0, 3).equals(user.getWashingRoom())) {
                List<NotifiReservation> notifiReservations = notifiReservationRepository.findByUserId(reservation.getUserId());
                for (NotifiReservation notifi : notifiReservations) {
                    saveNotificationHistory(reservation.getUserId(), machineName);
                    firebaseService.sendFcmNotification(notifi.getToken(), machineName);
                }
            }
        }
    }

    // 알림 기록 저장
    private void saveNotificationHistory(Long userId, String machineName) {
        NotifiHistory history = NotifiHistory.builder()
                .userId(userId)
                .machine(machineName)
                .date(LocalDate.now())
                .build();
        notifiHistoryRepository.save(history);
    }
}