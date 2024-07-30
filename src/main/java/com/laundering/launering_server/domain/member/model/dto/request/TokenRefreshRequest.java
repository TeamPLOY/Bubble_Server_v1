package com.laundering.launering_server.domain.member.model.dto.request;
import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequest(@NotBlank String refreshToken)
{
}

