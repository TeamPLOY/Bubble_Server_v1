package com.laundering.laundering_server.domain.notification.controller;

import com.laundering.laundering_server.domain.facade.NotificationFacade;
import com.laundering.laundering_server.domain.notification.model.dto.response.NotificationDetailResponse;
import com.laundering.laundering_server.domain.notification.model.dto.response.NotificationResponse;
import com.laundering.laundering_server.domain.notification.model.dto.response.ReservationLogResponse;
import com.laundering.laundering_server.domain.notification.model.dto.request.saveNotificationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static com.laundering.laundering_server.common.util.AuthenticationUtil.getMemberId;

@Tag(name = "알림")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationFacade notificationFacade;

    @Operation(summary = "공지사항")
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotification() {
        return ResponseEntity.ok(notificationFacade.getNotification());
    }

    @Operation(summary = "공지 세부사항")
    @GetMapping("/detail")
    public ResponseEntity<List<NotificationDetailResponse>> getNotificationDetail() {
        return ResponseEntity.ok(notificationFacade.getNotificationDetail());

    }

    @Operation(summary = "예약 사용기록")
    @GetMapping("/history")
    public ResponseEntity<List<ReservationLogResponse>> getReservationHistory() {
        return ResponseEntity.ok(notificationFacade.getReservationHistory(getMemberId()));

    }

    @Operation(summary = "알림 선택")
    @PostMapping("/save")
    public ResponseEntity<Void> saveNotification(
            saveNotificationRequest saveNotificationRequest
    ){
        notificationFacade.saveNotification(saveNotificationRequest,getMemberId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "알림 조회")
    @PostMapping("/check")
    public ResponseEntity<Boolean> getResNotification(
            saveNotificationRequest saveNotificationRequest
    ){
        return ResponseEntity.ok(notificationFacade.getResNotification(saveNotificationRequest,getMemberId()));
    }
}
