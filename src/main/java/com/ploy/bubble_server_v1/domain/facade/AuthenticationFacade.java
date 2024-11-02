package com.ploy.bubble_server_v1.domain.facade;


import com.ploy.bubble_server_v1.common.exception.BusinessException;
import com.ploy.bubble_server_v1.common.exception.ErrorCode;
import com.ploy.bubble_server_v1.domain.user.model.dto.request.SignUpRequest;
import com.ploy.bubble_server_v1.domain.user.model.dto.request.TokenRefreshRequest;
import com.ploy.bubble_server_v1.domain.user.model.dto.response.LoginResponse;
import com.ploy.bubble_server_v1.domain.user.model.dto.response.TokenResponse;
import com.ploy.bubble_server_v1.domain.user.model.dto.response.UserResponse;
import com.ploy.bubble_server_v1.domain.user.service.AuthService;
import com.ploy.bubble_server_v1.domain.user.service.UserService;
import com.ploy.bubble_server_v1.domain.user.model.dto.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AuthenticationFacade {
    private final AuthService authService; // AuthService 의존성 주입
    private final UserService userService; // UserService 의존성 주입
    @Transactional
    public LoginResponse login(LoginRequest req) {
        return authService.login(req.getEmail(), req.getPassword());
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

    public UserResponse getUserInfo(Long memberId) {
        return userService.getUserInfo(memberId);
    }
    @Transactional
    public void deleteUser(Long memberId) {
        userService.deleteUser(memberId);
    }

    @Transactional
    public void signUp(SignUpRequest req) {
        try {
            userService.create(req);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.DUPLICATED);
        }
    }
}

