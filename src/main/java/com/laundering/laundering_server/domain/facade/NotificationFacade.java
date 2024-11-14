package com.laundering.laundering_server.domain.facade;

import com.laundering.laundering_server.domain.notification.model.dto.request.saveNotificationRequest;
import com.laundering.laundering_server.domain.notification.model.dto.response.NotificationDetailResponse;
import com.laundering.laundering_server.domain.notification.model.dto.response.NotificationResponse;
import com.laundering.laundering_server.domain.notification.model.dto.response.ReservationLogResponse;
import com.laundering.laundering_server.domain.notification.service.NotificationService;
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
    public List<ReservationLogResponse> getReservationHistory(Long id) {
        return notificationService.getReservationHistory(id);
    }

    @Transactional
    public void saveNotification(saveNotificationRequest request, Long id) {
        notificationService.saveNotification(request,id);
    }
}
