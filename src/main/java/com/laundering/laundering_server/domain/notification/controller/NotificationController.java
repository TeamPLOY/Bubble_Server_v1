package com.laundering.laundering_server.domain.notification.controller;

import com.laundering.laundering_server.domain.facade.EmailFacade;
import com.laundering.laundering_server.domain.facade.NotificationFacade;
import com.laundering.laundering_server.domain.member.model.dto.request.*;
import com.laundering.laundering_server.domain.notification.model.dto.response.NotificationDetailResponse;
import com.laundering.laundering_server.domain.notification.model.dto.response.NotificationResponse;
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
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationFacade notificationFacade;

    @Operation(summary = "공지사항")
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotification() {
        return ResponseEntity.ok(notificationFacade.getNotification());
    }

    @Operation(summary = "공지사항")
    @GetMapping("/detail")
    public ResponseEntity<List<NotificationDetailResponse>> getNotificationDetail() {
        return ResponseEntity.ok(notificationFacade.getNotificationDetail());

    }
}
