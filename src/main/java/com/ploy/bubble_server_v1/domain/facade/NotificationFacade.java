package com.ploy.bubble_server_v1.domain.facade;

import com.ploy.bubble_server_v1.domain.notification.model.dto.request.saveNotificationRequest;
import com.ploy.bubble_server_v1.domain.notification.model.dto.response.NotificationDetailResponse;
import com.ploy.bubble_server_v1.domain.notification.model.dto.response.NotificationResponse;
import com.ploy.bubble_server_v1.domain.notification.model.dto.response.ReservationLogResponse;
import com.ploy.bubble_server_v1.domain.notification.service.NotificationService;
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

    @Transactional
    public boolean getResNotification(saveNotificationRequest request, Long id) {
        return notificationService.getResNotification(request,id);
    }
}
