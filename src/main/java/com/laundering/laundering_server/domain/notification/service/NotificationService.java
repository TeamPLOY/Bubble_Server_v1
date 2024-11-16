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

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
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

    public List<ReservationLogResponse> getReservationHistory(Long id) {
        // 사용자 정보 조회
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNKNOWN_ERROR));

        // 해당 사용자의 예약 기록 조회
        List<Reservation> reservation = reservationRepository.findByUserId(id);

        // 예약 기록을 ReservationLogResponse로 변환하면서 washingRoom과 요일 추가
        return reservation.stream()
                .map(log -> {
                    // 날짜에서 요일을 추출
                    DayOfWeek dayOfWeek = log.getDate().getDayOfWeek();
                    String dayOfWeekStr = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN); // 요일을 한글로 반환

                    return new ReservationLogResponse(
                            log.getDate(),         // LocalDate를 String으로 변환
                            log.isCancel(),        // 취소 여부 전달
                            user.getWashingRoom(), // 사용자의 washingRoom 추가
                            dayOfWeekStr,          // 요일 정보 추가
                            log.getMachine()       // 추가된 machine 정보
                    );
                })
                .collect(Collectors.toList());
    }

}