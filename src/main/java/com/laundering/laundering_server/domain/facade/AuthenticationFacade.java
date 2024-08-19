package com.laundering.laundering_server.domain.facade;


import com.laundering.laundering_server.common.socialPlatform.BsmClient;
import com.laundering.laundering_server.domain.member.model.dto.response.LoginResponse;
import com.laundering.laundering_server.domain.member.model.dto.response.TokenResponse;
import com.laundering.laundering_server.domain.member.model.dto.response.UserResponse;
import com.laundering.laundering_server.domain.member.service.AuthService;
import com.laundering.laundering_server.domain.member.service.UserService;
import com.laundering.laundering_server.domain.member.model.dto.request.TokenRefreshRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AuthenticationFacade {
    private final AuthService authService; // AuthService 의존성 주입
    private final UserService userService; // UserService 의존성 주입
    private final BsmClient bsmClient; // KakaoClient 의존성 주입

    @Transactional
    public LoginResponse loginBsm(String accessToken) {
        var userInfo = bsmClient.getUserInfo(accessToken);

        // 사용자 정보를 기반으로 로그인 처리 수행
        return loginProcess(userInfo.userId());
    }

    @Transactional // 이 메소드는 트랜잭션이 필요하다는 것을 나타냄
    public void logout(Long memberId) {
        // AuthService의 logout 메소드를 호출하여 로그아웃 처리 수행
        authService.logout(memberId);
    }

    @Transactional
    public TokenResponse refreshToken(TokenRefreshRequest tokenRefreshRequest)
    {
        return authService.refreshToken(tokenRefreshRequest);
    }

    // 주어진 소셜 플랫폼 타입과 사용자 ID를 통해 로그인 처리 수행
    private LoginResponse loginProcess(String socialUid) {
        // 소셜 계정 정보로 회원을 조회
        var member = userService.getBySocialAccount(socialUid);

        // 회원이 존재하는 경우    @Transactional
        if (member != null) {
            return authService.getLoginResponse(member);
        }

        // 회원이 존재하지 않는 경우, 새 회원 생성
        var newMember = userService.createBySocialAccount(socialUid);

        // 새 회원에 대한 로그인 응답 생성
        return authService.getLoginResponse(newMember);
    }

    public UserResponse getUserInfo(Long memberId) {
        return userService.getUserInfo(memberId);
    }
}

