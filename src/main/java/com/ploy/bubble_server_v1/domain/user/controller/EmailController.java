package com.ploy.bubble_server_v1.domain.user.controller;

import com.ploy.bubble_server_v1.domain.facade.EmailFacade;
import com.ploy.bubble_server_v1.domain.user.model.dto.request.EmailCheckRequest;
import com.ploy.bubble_server_v1.domain.user.model.dto.request.EmailRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "이메일 인증")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")

public class EmailController {
    private final EmailFacade emailFacade;

    @Operation(summary = "이메일 전송")
    @PostMapping("/send")
    public ResponseEntity<Void> sendEmail(@RequestBody EmailRequest emailRequest) {
        emailFacade.sendEmail(emailRequest);
        return ResponseEntity.noContent().build();
    }

    // 이 이메일은 스케줄러 사용해서 5분 뒤 디비에서 삭제
    @Operation(summary = "이메일 인증")
    @PostMapping("/verify")
    public ResponseEntity<Boolean> certificationEmail(@RequestBody EmailCheckRequest emailCheckRequest){
        return ResponseEntity.ok(emailFacade.certificationEmail(emailCheckRequest));
    }
}