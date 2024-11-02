package com.ploy.bubble_server_v1.domain.user.model.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
