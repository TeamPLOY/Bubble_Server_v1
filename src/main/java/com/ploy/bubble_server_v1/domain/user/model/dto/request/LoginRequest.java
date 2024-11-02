package com.ploy.bubble_server_v1.domain.user.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest
{
    @Schema(description = "이메일")
    private String email;

    @Schema(description = "패스워드")
    private String password;
}