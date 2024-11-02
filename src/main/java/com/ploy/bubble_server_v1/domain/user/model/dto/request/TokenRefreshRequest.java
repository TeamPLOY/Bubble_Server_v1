package com.ploy.bubble_server_v1.domain.user.model.dto.request;
import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequest(@NotBlank String refreshToken)
{
}

