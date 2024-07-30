package com.laundering.laundering_server.domain.member.model.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
