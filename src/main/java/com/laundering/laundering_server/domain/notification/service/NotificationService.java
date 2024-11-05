package com.laundering.laundering_server.domain.notification.service;

import com.laundering.laundering_server.common.exception.BusinessException;
import com.laundering.laundering_server.common.exception.ErrorCode;
import com.laundering.laundering_server.domain.member.model.entity.Users;
import com.laundering.laundering_server.domain.member.repository.UsersRepository;
import com.laundering.laundering_server.domain.notification.model.dto.response.NotificationDetailResponse;
import com.laundering.laundering_server.domain.notification.model.dto.response.NotificationResponse;
import com.laundering.laundering_server.domain.notification.model.dto.response.ReservationLogResponse;
import com.laundering.laundering_server.domain.notification.model.entity.NotifiReservation;
import com.laundering.laundering_server.domain.notification.repository.NotifiReservationRepository;
import com.laundering.laundering_server.domain.notification.repository.NotificationRepository;
import com.laundering.laundering_server.domain.washingMachine.model.entity.Reservation;
import com.laundering.laundering_server.domain.notification.model.dto.request.saveNotificationRequest;
import com.laundering.laundering_server.domain.washingMachine.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService
{
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private NotifiReservationRepository notifiReservationRepository;

    @Autowired
    private UsersRepository usersRepository;

    public List<NotificationResponse> getNotification() {
        return notificationRepository.findAll().stream()
                .map(notification -> new NotificationResponse(notification.getTitle(), notification.getDate()))
                .collect(Collectors.toList());
    }
    public List<NotificationDetailResponse> getNotificationDetail() {
        return notificationRepository.findAll().stream()
                .map(notification -> new NotificationDetailResponse(
                        notification.getTitle(),
                        notification.getDetail(),
                        notification.getDate()))
                .collect(Collectors.toList());
    }

//    public List<ReservationLogResponse> getReservationHistory(Long id) {
//        // 사용자 정보 조회
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new BusinessException(ErrorCode.UNKNOWN_ERROR));
//
//        // 해당 사용자의 예약 기록 조회
//        List<ReservationLog> reservationLogs = reservationLogRepository.findByUserId(id);
//
//        // 예약 기록을 ReservationLogResponse로 변환하면서 washingRoom 추가
//        return reservationLogs.stream()
//                .map(log -> new ReservationLogResponse(
//                        log.getDate().toLocalDate(),       // LocalDateTime에서 LocalDate로 변환
//                        log.getResDate().toString(),       // LocalDate를 String으로 변환
//                        log.isCancel(),                   // 취소 여부 전달
//                        user.getWashingRoom()             // 사용자의 washingRoom 추가
//                ))
//                .collect(Collectors.toList());
//    }

    public List<ReservationLogResponse> getReservationHistory(Long id) {
        // 사용자 정보 조회
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNKNOWN_ERROR));

        // 해당 사용자의 예약 기록 조회
        List<Reservation> reservation = reservationRepository.findByUserId(id);

        // 예약 기록을 ReservationLogResponse로 변환하면서 washingRoom 추가
        return reservation.stream()
                .map(log -> new ReservationLogResponse(
                        log.getDate(),       // LocalDate를 String으로 변환
                        log.isCancel(),                   // 취소 여부 전달
                        user.getWashingRoom()             // 사용자의 washingRoom 추가
                ))
                .collect(Collectors.toList());
    }

    public void saveNotification(saveNotificationRequest request, Long userId) {
        // userId와 machine으로 NotifiReservation 조회
        Optional<NotifiReservation> existingNotification = notifiReservationRepository.findByUserIdAndMachine(userId, request.machine());

        // 기존 알림이 존재하면 삭제 후 함수 종료
        if (existingNotification.isPresent()) {
            notifiReservationRepository.delete(existingNotification.get());
            return;  // 알림 삭제 후 함수 종료
        }
        
        // 새로운 NotifiReservation 엔티티 생성
        NotifiReservation notifiReservation = NotifiReservation.builder()
                .userId(userId)
                .machine(request.machine())
                .date(LocalDate.now()) // 현재 날짜로 설정
                .build();

        // DB에 저장
        notifiReservationRepository.save(notifiReservation);
    }


    public boolean getResNotification(saveNotificationRequest request, Long userId) {
        // userId와 요청된 machine을 기준으로 NotifiReservation 조회
        Optional<NotifiReservation> reservation = notifiReservationRepository.findByUserIdAndMachine(userId, request.machine());

        // 예약 알림이 존재하면 true 반환, 존재하지 않으면 false 반환
        return reservation.isPresent();
    }
}