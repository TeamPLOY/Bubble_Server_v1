package com.laundering.laundering_server.domain.notification.service;

import com.laundering.laundering_server.domain.member.repository.UserRepository;
import com.laundering.laundering_server.domain.notification.model.dto.response.NotificationDetailResponse;
import com.laundering.laundering_server.domain.notification.model.dto.response.NotificationResponse;
import com.laundering.laundering_server.domain.notification.repository.NotificationRepository;
import com.laundering.laundering_server.domain.washingMachine.model.entity.ReservationLog;
import com.laundering.laundering_server.domain.washingMachine.repository.ReservationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService
{
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ReservationLogRepository reservationLogRepository;

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


    public List<ReservationLog> getReservationHistory(Long userId) {
        // userId로 예약 로그 조회
        return reservationLogRepository.findByUserId(userId);
    }
}



