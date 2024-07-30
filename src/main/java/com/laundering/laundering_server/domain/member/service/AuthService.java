package com.laundering.laundering_server.domain.member.service;

import com.laundering.laundering_server.common.exception.BusinessException;
import com.laundering.laundering_server.common.exception.ErrorCode;
import com.laundering.laundering_server.common.jwt.Jwt;
import com.laundering.laundering_server.domain.member.model.dto.request.TokenRefreshRequest;
import com.laundering.laundering_server.domain.member.model.dto.response.LoginResponse;
import com.laundering.laundering_server.domain.member.model.dto.response.TokenResponse;
import com.laundering.laundering_server.domain.member.repository.UserRepository;
import com.laundering.laundering_server.domain.member.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService
{
    private final UserRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final Jwt jwt;


    @Transactional
    public void logout(Long userId)
    {
        var user = memberRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        user.setRefreshToken(""); // setRefreshToken 메서드 호출
    }

    public LoginResponse getLoginResponse(User member)
    {
        var tokens = publishToken(user);
        if (member.getEmail() == null || member.getEmail().isEmpty()) {
            return new LoginResponse(tokens, true); //isNewUser은 떄떄로 필요함

        }
        else {
            return new LoginResponse(tokens, false);
        }
    }

    @Transactional
    public TokenResponse publishToken(Member member)
    {
        TokenResponse tokenResponse = jwt.generateAllToken(
                Jwt.Claims.from(
                        member.getId()
                ));

        member.setRefreshToken(tokenResponse.refreshToken());

        return tokenResponse;
    }

    @Transactional
    public TokenResponse refreshToken(TokenRefreshRequest tokenRefreshRequest)
    {
        var member = memberRepository.findByRefreshToken(tokenRefreshRequest.refreshToken());
        if (member.isPresent() == false)
        {
            throw new AccessDeniedException("refresh token 이 만료되었습니다.");
        }

        Long memberId;
        String[] roles;

        try
        {
            Jwt.Claims claims = jwt.verify(member.get().getRefreshToken());
            memberId = claims.getMemberId();
        }
        catch (Exception e)
        {
            log.warn("Jwt 처리중 문제가 발생하였습니다 : {}", e.getMessage());
            throw e;
        }
        TokenResponse tokenResponse = jwt.generateAllToken(Jwt.Claims.from(memberId));

        member.get().setRefreshToken(tokenResponse.refreshToken());

        return tokenResponse;
    }

}

