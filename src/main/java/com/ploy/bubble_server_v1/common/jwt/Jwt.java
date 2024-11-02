package com.ploy.bubble_server_v1.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ploy.bubble_server_v1.domain.user.model.dto.response.TokenResponse;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

public class Jwt {
    private final String issuer;
    private final int tokenExpire;
    private final int refreshTokenExpire;
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    public Jwt(String clientSecret, String issuer, int tokenExpire, int refreshTokenExpire) {
        this.issuer = issuer;
        this.tokenExpire = tokenExpire;
        this.refreshTokenExpire = refreshTokenExpire;
        this.algorithm = Algorithm.HMAC512(clientSecret);
        this.jwtVerifier = JWT.require(algorithm)
                .withIssuer(issuer).build();
    }

    // 액세스 토큰을 생성하는 메서드입니다. Claims 객체를 매개변수로 받아서 서명합니다.
    public String generateAccessToken(Claims claims) {
        return sign(claims, tokenExpire);
    }

    public String generateRefreshToken(Claims claims) {
        return sign(claims, refreshTokenExpire);
    }

    public TokenResponse generateAllToken(Claims claims) {
        return new TokenResponse(generateAccessToken(claims), generateRefreshToken(claims));
    }

    private String sign(Claims claims, int expireTime) {
        Date now = new Date(); // 현재 시간을 가져옵니다.
        return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + expireTime * 1000L))
                .withClaim("memberId", claims.memberId)
                .sign(algorithm);
    }

    public Claims verify(String token) {
        return new Claims(jwtVerifier.verify(token));
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Claims {
        Long memberId;
        Date iat;
        Date exp;

        Claims(DecodedJWT decodedJwt) {
            Claim memberId = decodedJwt.getClaim("memberId");
            if (!memberId.isNull()) {
                this.memberId = memberId.asLong();
            }
            this.iat = decodedJwt.getIssuedAt();
            this.exp = decodedJwt.getExpiresAt();
        }

        public static Claims from(Long memberId) {
            Claims claims = new Claims();
            claims.memberId = memberId;
            return claims;
        }

        @Override
        public String toString() {
            return "Claims{"
                    + "memberId=" + memberId
                    + ", iat=" + iat
                    + ", exp="
                    + exp
                    + '}';
        }
    }
}

