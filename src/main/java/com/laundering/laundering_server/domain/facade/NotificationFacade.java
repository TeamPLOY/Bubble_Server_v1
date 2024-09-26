package com.laundering.laundering_server.domain.facade;

import com.laundering.laundering_server.domain.notification.model.dto.response.NotificationDetailResponse;
import com.laundering.laundering_server.domain.notification.model.dto.response.NotificationResponse;
import com.laundering.laundering_server.domain.notification.service.NotificationService;
import com.laundering.laundering_server.domain.washingMachine.model.entity.ReservationLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationFacade {
    private final NotificationService notificationService;

    @Transactional
    public List<NotificationResponse> getNotification() {
        return notificationService.getNotification();
    }

    @Transactional
    public List<NotificationDetailResponse> getNotificationDetail() {
        return notificationService.getNotificationDetail();
    }

    @Transactional
    public List<ReservationLog> getReservationHistory(Long id) {
        return notificationService.getReservationHistory(id);
    }


}
