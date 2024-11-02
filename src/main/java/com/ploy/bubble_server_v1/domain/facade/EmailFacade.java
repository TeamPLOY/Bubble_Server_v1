package com.ploy.bubble_server_v1.domain.facade;

import com.ploy.bubble_server_v1.domain.user.model.dto.request.EmailCheckRequest;
import com.ploy.bubble_server_v1.domain.user.model.dto.request.EmailRequest;
import com.ploy.bubble_server_v1.domain.user.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Component
@RequiredArgsConstructor
public class EmailFacade {
    private final EmailService emailService;

    @Transactional
    public void sendEmail(EmailRequest req) {
        emailService.sendEmail(req.email());
    }

    @Transactional
    public boolean certificationEmail(EmailCheckRequest req) {
        return emailService.certificationEmail(req.code(),req.email());
    }
}
