package com.laundering.laundering_server.domain.member.controller;


import com.laundering.laundering_server.domain.facade.AuthenticationFacade;
import com.laundering.laundering_server.domain.member.model.dto.request.TokenRefreshRequest;
import com.laundering.laundering_server.domain.member.model.dto.response.LoginResponse;
import com.laundering.laundering_server.domain.member.model.dto.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.laundering.laundering_server.common.util.AuthenticationUtil.getMemberId;
@Tag(name = "예약")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController
{
    private final AuthenticationFacade authenticationFacade;

    @Operation(summary = "BSM 로그인 / 가입")
    @GetMapping("/login")
    public ResponseEntity<LoginResponse> loginKakao(@RequestParam String code)
    {
        return ResponseEntity.ok(authenticationFacade.loginBsm(code));
    }

    @Operation(summary = "로그아웃")
    @PreAuthorize("hasAnyRole('USER')")
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
}