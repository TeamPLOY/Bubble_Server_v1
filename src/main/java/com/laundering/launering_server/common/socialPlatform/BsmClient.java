package com.laundering.launering_server.common.socialPlatform;


import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Bsm API와의 통신을 담당하는 클라이언트 클래스입니다.
 */
@Component
@RequiredArgsConstructor
public class BsmClient {
    // Bsm API의 사용자 정보 요청 URL
    private static final String GET_MEMBER_INFO_URL = "https://api-auth.bssm.app/api/oauth/resource";

    // HTTP 요청을 수행하기 위한 RestTemplate 객체
    private final RestTemplate restTemplate;

    /**
     * 액세스 토큰을 사용하여 Bsm 사용자 정보를 가져옵니다.
     *
     * @param accessToken Bsm OAuth2 액세스 토큰
     * @return SocialPlatformUserInfo 객체, 사용자 ID가 포함됨
     */
    public SocialPlatformUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        // 요청 헤더에 Content-Type과 Authorization 추가
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        // 요청 엔티티 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        // 사용자 정보 요청을 보내고 응답 받기
        ResponseEntity<String> response = restTemplate.exchange(
                GET_MEMBER_INFO_URL,
                HttpMethod.GET,
                request,
                String.class
        );

        // 응답 본문을 JSON 객체로 파싱
        JSONObject responseBody = new JSONObject(response.getBody());
        Long id = responseBody.getLong("id");
        String uid = id.toString();

        // SocialPlatformUserInfo 객체를 반환
        return new SocialPlatformUserInfo(uid, null);
    }
}
