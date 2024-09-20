package com.laundering.laundering_server.domain.member.controller;


import com.laundering.laundering_server.domain.facade.AuthenticationFacade;
import com.laundering.laundering_server.domain.member.model.dto.request.*;
import com.laundering.laundering_server.domain.member.model.dto.response.LoginResponse;
import com.laundering.laundering_server.domain.member.model.dto.response.TokenResponse;
import com.laundering.laundering_server.domain.member.model.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.laundering.laundering_server.common.util.AuthenticationUtil.getMemberId;
@Tag(name = "인증/인가")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController
{
    private final AuthenticationFacade authenticationFacade;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<String> signUpParent(
            @Valid @RequestBody SignUpRequest req) {
        authenticationFacade.signUp(req);
        return new ResponseEntity<>("회원가입이 완료되었습니다.", HttpStatus.OK);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(authenticationFacade.login(req));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout()
    {
        authenticationFacade.logout(getMemberId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "토큰 갱신")
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(
            @Valid @RequestBody TokenRefreshRequest tokenRefreshRequest
    ) {
        return ResponseEntity.ok(authenticationFacade.refreshToken(tokenRefreshRequest));
    }

    @Operation(summary = "사용자 정보 확인")
    @GetMapping("/user")
    public ResponseEntity<UserResponse> getUserInfo(){
        UserResponse userInfo = authenticationFacade.getUserInfo(getMemberId());
        return ResponseEntity.ok(userInfo);
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/user")
    public ResponseEntity<Void> deleteUser(){
        authenticationFacade.deleteUser(getMemberId());
        return ResponseEntity.noContent().build();
    }
}