package com.laundering.laundering_server.domain.facade;

import com.laundering.laundering_server.domain.member.model.dto.request.EmailCheckRequest;
import com.laundering.laundering_server.domain.member.model.dto.request.EmailRequest;
import com.laundering.laundering_server.domain.member.service.EmailService;
import com.laundering.laundering_server.domain.notification.model.dto.response.NotificationDetailResponse;
import com.laundering.laundering_server.domain.notification.model.dto.response.NotificationResponse;
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

}
