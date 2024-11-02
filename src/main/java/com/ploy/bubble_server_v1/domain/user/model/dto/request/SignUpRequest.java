package com.ploy.bubble_server_v1.domain.user.model.dto.request;

public record SignUpRequest(
    String email,

    String password,

    String name,

    int stuNum,

    String roomNum
) {
}
