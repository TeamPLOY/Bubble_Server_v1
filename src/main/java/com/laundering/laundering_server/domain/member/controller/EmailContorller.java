package com.laundering.laundering_server.domain.member.controller;

import com.laundering.laundering_server.domain.facade.EmailFacade;
import com.laundering.laundering_server.domain.member.model.dto.request.*;
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
public class EmailContorller {
    private final EmailFacade emailFacade;

    @Operation(summary = "이메일 전송")
    @PostMapping("/email")
    public ResponseEntity<Void> sendEmail(@RequestBody EmailRequest emailRequest) {
        emailFacade.sendEmail(emailRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "이메일 인증")
    @GetMapping("/email")
    public ResponseEntity<Void> certificationEmail(@RequestBody EmailCheckRequest EmailCheckRequest){
        emailFacade.certificationEmail(EmailCheckRequest);
        return ResponseEntity.noContent().build();
    }
}