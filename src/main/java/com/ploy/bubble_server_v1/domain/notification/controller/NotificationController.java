package com.ploy.bubble_server_v1.domain.notification.controller;

import com.ploy.bubble_server_v1.domain.facade.NotificationFacade;
import com.ploy.bubble_server_v1.domain.notification.model.dto.response.NotificationDetailResponse;
import com.ploy.bubble_server_v1.domain.notification.model.dto.response.NotificationResponse;
import com.ploy.bubble_server_v1.domain.notification.model.dto.request.saveNotificationRequest;
import com.ploy.bubble_server_v1.common.util.AuthenticationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "알림")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NotificationController {
    private final NotificationFacade notificationFacade;

    @Operation(summary = "공지사항 목록 조회")
    @GetMapping("/announcements")
    public ResponseEntity<List<NotificationResponse>> getNotification() {
        return ResponseEntity.ok(notificationFacade.getNotification());
    }

    @Operation(summary = "공지 세부사항")
    @GetMapping("/announcements/{id}")
    public ResponseEntity<List<NotificationDetailResponse>> getNotificationDetail() {
        return ResponseEntity.ok(notificationFacade.getNotificationDetail());

    }
    @Operation(summary = "알림 선택")
    @PostMapping("/notifications")
    public ResponseEntity<Void> saveNotification(
            saveNotificationRequest saveNotificationRequest
    ){
        notificationFacade.saveNotification(saveNotificationRequest, AuthenticationUtil.getMemberId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "알림 조회")
    @GetMapping("/notifications")
    public ResponseEntity<Boolean> getResNotification(
            saveNotificationRequest saveNotificationRequest
    ){
        return ResponseEntity.ok(notificationFacade.getResNotification(saveNotificationRequest, AuthenticationUtil.getMemberId()));
    }
}
