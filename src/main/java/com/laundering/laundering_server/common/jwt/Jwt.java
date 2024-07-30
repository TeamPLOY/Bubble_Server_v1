package com.laundering.laundering_server.common.jwt;

// JWT 관련 기능을 제공하기 위해 'com.auth0.jwt'와 관련된 클래스를 임포트합니다.
// 'TokenResponse' 클래스는 JWT 토큰을 담아 응답하기 위한 클래스입니다.
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.laundering.laundering_server.domain.member.model.dto.response.TokenResponse;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

public class Jwt {
    // JWT 생성 및 검증에 필요한 필드를 선언합니다.
    private final String issuer; // 토큰을 발행한 주체를 나타내는 문자열
    private final int tokenExpire; // 액세스 토큰의 만료 시간 (초 단위)
    private final int refreshTokenExpire; // 리프레시 토큰의 만료 시간 (초 단위)
    private final Algorithm algorithm; // JWT 서명에 사용할 알고리즘
    private final JWTVerifier jwtVerifier; // JWT 검증에 사용할 검증기

    // Jwt 클래스의 생성자입니다. 비밀 키, 발행자, 액세스 토큰 만료 시간, 리프레시 토큰 만료 시간을 매개변수로 받습니다.
    public Jwt(String clientSecret, String issuer, int tokenExpire, int refreshTokenExpire) {
        this.issuer = issuer; // 발행자를 설정합니다.
        this.tokenExpire = tokenExpire; // 액세스 토큰 만료 시간을 설정합니다.
        this.refreshTokenExpire = refreshTokenExpire; // 리프레시 토큰 만료 시간을 설정합니다.
        this.algorithm = Algorithm.HMAC512(clientSecret); // 비밀 키를 사용하여 서명 알고리즘을 설정합니다.
        // JWT 검증기를 설정합니다. 발행자와 알고리즘을 설정하여 검증기를 생성합니다.
        this.jwtVerifier = JWT.require(algorithm)
                .withIssuer(issuer).build();
    }

    // 액세스 토큰을 생성하는 메서드입니다. Claims 객체를 매개변수로 받아서 서명합니다.
    public String generateAccessToken(Claims claims) {
        return sign(claims, tokenExpire); // 액세스 토큰을 생성하고 반환합니다.
    }

    // 리프레시 토큰을 생성하는 메서드입니다. Claims 객체를 매개변수로 받아서 서명합니다.
    public String generateRefreshToken(Claims claims) {
        return sign(claims, refreshTokenExpire); // 리프레시 토큰을 생성하고 반환합니다.
    }

    // 액세스 토큰과 리프레시 토큰을 모두 생성하여 TokenResponse 객체로 반환하는 메서드입니다.
    public TokenResponse generateAllToken(Claims claims) {
        return new TokenResponse(generateAccessToken(claims), generateRefreshToken(claims));
        // 액세스 토큰과 리프레시 토큰을 생성하고, TokenResponse 객체에 담아 반환합니다.
    }

    // Claims 객체와 만료 시간을 받아 JWT를 생성하는 메서드입니다.
    private String sign(Claims claims, int expireTime) {
        Date now = new Date(); // 현재 시간을 가져옵니다.
        // JWT를 생성하고 서명합니다.
        return JWT.create()
                .withIssuer(issuer) // 발행자를 설정합니다.
                .withIssuedAt(now) // 발행 시간을 설정합니다.
                .withExpiresAt(new Date(now.getTime() + expireTime * 1000L)) // 만료 시간을 설정합니다.
                .withClaim("memberId", claims.memberId) // 'memberId' 클레임을 설정합니다.
                .sign(algorithm); // JWT를 서명합니다.
    }

    // 주어진 JWT 토큰을 검증하고 Claims 객체를 반환하는 메서드입니다.
    public Claims verify(String token) {
        return new Claims(jwtVerifier.verify(token)); // 토큰을 검증하고 Claims 객체로 변환하여 반환합니다.
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Claims {
        Long memberId; // 회원 ID
        Date iat; // 토큰 발행 시간
        Date exp; // 토큰 만료 시간

        // DecodedJWT 객체에서 클레임을 추출하여 Claims 객체를 생성하는 생성자입니다.
        Claims(DecodedJWT decodedJwt) {
            Claim memberId = decodedJwt.getClaim("memberId"); // 'memberId' 클레임을 가져옵니다.
            if (!memberId.isNull()) {
                this.memberId = memberId.asLong(); // 클레임 값이 null이 아니면 memberId를 설정합니다.
            }
            this.iat = decodedJwt.getIssuedAt(); // 발행 시간을 설정합니다.
            this.exp = decodedJwt.getExpiresAt(); // 만료 시간을 설정합니다.
        }

        // 주어진 회원 ID와 역할 배열로 Claims 객체를 생성하는 정적 메서드입니다.
        public static Claims from(Long memberId) {
            Claims claims = new Claims(); // Claims 객체를 생성합니다.
            claims.memberId = memberId; // memberId를 설정합니다.
            return claims; // Claims 객체를 반환합니다.
        }

        // Claims 객체를 문자열 형태로 표현하는 오버라이드된 toString() 메서드입니다.
        @Override
        public String toString() {
            return "Claims{"
                    + "memberId=" + memberId
                    + ", roles="
                    + ", iat=" + iat
                    + ", exp="
                    + exp
                    + '}';
        }
    }
}

