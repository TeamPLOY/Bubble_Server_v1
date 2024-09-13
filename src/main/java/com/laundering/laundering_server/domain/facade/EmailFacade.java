package com.laundering.laundering_server.domain.facade;

import com.laundering.laundering_server.domain.member.model.dto.request.EmailCheckRequest;
import com.laundering.laundering_server.domain.member.model.dto.request.EmailRequest;
import com.laundering.laundering_server.domain.member.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Component
@RequiredArgsConstructor
public class EmailFacade {
    private final EmailService emailService;

    @Transactional
    public void sendEmail(EmailRequest req) {
        emailService.sendEmail(req.Email());
    }

    @Transactional
    public void certificationEmail(EmailCheckRequest req) {
        emailService.certificationEmail(req.code(),req.email());
    }
}
