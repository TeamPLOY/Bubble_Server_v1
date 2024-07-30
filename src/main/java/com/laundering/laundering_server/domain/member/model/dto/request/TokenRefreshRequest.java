package com.laundering.laundering_server.domain.member.model.dto.request;
import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequest(@NotBlank String refreshToken)
{
}

