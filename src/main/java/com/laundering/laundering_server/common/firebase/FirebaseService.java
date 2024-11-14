package com.laundering.laundering_server.common.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.AccessToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirebaseService {

    private final String serviceAccountKeyFile = "/Users/hantaeyeongmac/bubble-457b0-firebase-adminsdk-a39g3-34a394f7a4.json"; // 서비스 계정 키 파일 경로

    // 액세스 토큰을 발급받는 메서드
    public String getAccessToken() throws IOException {
        // 서비스 계정 키를 사용하여 GoogleCredentials 객체를 생성
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(serviceAccountKeyFile))
                .createScoped("https://www.googleapis.com/auth/firebase.messaging", "https://www.googleapis.com/auth/cloud-platform");

        // 액세스 토큰을 발급
        AccessToken accessToken = credentials.refreshAccessToken();
        return accessToken.getTokenValue(); // 발급받은 액세스 토큰 반환
    }

    // FCM 알림을 전송하는 메서드
    public void sendFcmNotification(String token, String machineName) {
        String title = "버블 알림";
        String body = "세탁실 " + machineName + "가 곧 완료됩니다.";

        // FCM 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            // 액세스 토큰을 발급받아 헤더에 포함
            headers.setBearerAuth(getAccessToken());
        } catch (IOException e) {
            log.error("액세스 토큰 발급 실패: {}", e.getMessage());
            return;
        }

        // FCM 요청 바디 설정 (수정된 JSON 형식)
        Map<String, Object> message = new HashMap<>();
        message.put("message", Map.of(
                "token", token,
                "notification", Map.of(
                        "title", title,
                        "body", body
                )
        ));

        // 요청 생성
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(message, headers);

        // FCM 서버로 요청 전송
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://fcm.googleapis.com/v1/projects/bubble-457b0/messages:send",
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            log.info("FCM Response: {}", response.getBody());
        } catch (Exception e) {
            log.error("FCM 알림 전송 실패: {}", e.getMessage());
        }
    }
}
