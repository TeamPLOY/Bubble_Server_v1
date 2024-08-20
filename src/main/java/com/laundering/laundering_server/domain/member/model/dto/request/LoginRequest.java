package com.laundering.laundering_server.domain.member.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest
{
    @Schema(description = "AccessToken")
    private String accessToken;
}
