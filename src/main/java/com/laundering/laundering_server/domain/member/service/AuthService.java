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
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final Jwt jwt;

    public LoginResponse login(String email, String password) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));

            // 입력된 비밀번호와 저장된 비밀번호 해시를 비교하여 일치 여부를 확인합니다.
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BusinessException(ErrorCode.UNAUTHORIZED);
            }

            // 비밀번호가 일치하면 로그인 응답 객체를 생성하여 반환합니다.
            return getLoginResponse(user);
        } catch (BusinessException e) {
            log.error("로그인 실패: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("로그인 중 알 수 없는 오류 발생: {}", e.getMessage());
            throw new BusinessException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    @Transactional
    public void logout(Long userId)
    {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        user.setRefreshToken(""); // setRefreshToken 메서드 호출
    }

    public LoginResponse getLoginResponse(User user)
    {
        var tokens = publishToken(user);
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return new LoginResponse(tokens); //isNewUser은 떄떄로 필요함

        }
        else {
            return new LoginResponse(tokens);
        }
    }

    @Transactional
    public TokenResponse publishToken(User user)
    {
        TokenResponse tokenResponse = jwt.generateAllToken(
                Jwt.Claims.from(
                        user.getId()
                ));

        user.setRefreshToken(tokenResponse.refreshToken());

        return tokenResponse;
    }

    @Transactional
    public TokenResponse refreshToken(TokenRefreshRequest tokenRefreshRequest)
    {
        var user = userRepository.findByRefreshToken(tokenRefreshRequest.refreshToken());
        if (user.isPresent() == false)
        {
            throw new AccessDeniedException("refresh token 이 만료되었습니다.");
        }

        Long memberId;

        try
        {
            Jwt.Claims claims = jwt.verify(user.get().getRefreshToken());
            memberId = claims.getMemberId();
        }
        catch (Exception e)
        {
            log.warn("Jwt 처리중 문제가 발생하였습니다 : {}", e.getMessage());
            throw e;
        }
        TokenResponse tokenResponse = jwt.generateAllToken(Jwt.Claims.from(memberId));

        user.get().setRefreshToken(tokenResponse.refreshToken());

        return tokenResponse;
    }

}

