package com.laundering.laundering_server.domain.member.service;

import com.laundering.laundering_server.domain.member.model.entity.Email;
import com.laundering.laundering_server.domain.member.repository.EmailRepository;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    @Autowired
    private EmailRepository emailRepository; // 이메일 정보를 저장할 리포지토리

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private String port;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${spring.mail.password}")
    private String senderPassword;

    /**
     * 이메일로 6자리 랜덤 코드 발송 및 정보 저장
     *
     * @param email 수신자 이메일 주소
     */
    public void sendEmail(String email) {
        // 6자리 랜덤 숫자 생성
        Random random = new Random();
        int randomCode = 100000 + random.nextInt(900000); // 100000 ~ 999999 범위의 숫자 생성

        // 이메일 보내기
        send(email, randomCode); // 실제 이메일 전송 로직 호출

        // 이메일 정보 저장
        Email emailEntity = Email.builder()
                .email(email)
                .code(randomCode)
                .date(LocalDate.now()) // 현재 날짜 설정
                .build();

        emailRepository.save(emailEntity); // 이메일 정보 데이터베이스에 저장
    }

    private void send(String recipientEmail, int code) {
        // SMTP 서버 설정
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // 이메일 세션 생성
        Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {
            @Override
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // 템플릿 파일 읽기
            String template = Files.readString(Paths.get("src/main/resources/templates/email-template.html"), StandardCharsets.UTF_8);
            String htmlContent = template.replace("{{code}}", String.valueOf(code));

            // 이메일 메시지 작성
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject("버블 회원가입 인증 메일");
            message.setContent(htmlContent, "text/html; charset=utf-8");

            // 이메일 전송
            Transport.send(message);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }



    public boolean certificationEmail(int code, String email) {
        // email로 가장 최근의 코드 조회
        Optional<Email> verification = emailRepository.findTop1ByEmailOrderByIdDesc(email);
        log.info(verification.toString());
        // 검증할 email 데이터가 있는지 확인
        if (verification.isPresent()) {
            Email emailEntity = verification.get();
            // 저장된 코드와 입력된 코드를 비교
            return emailEntity.getCode() == code;
        } else {
            // 해당 이메일에 대한 코드가 없을 경우 false 반환
            return false;
        }
    }

}
