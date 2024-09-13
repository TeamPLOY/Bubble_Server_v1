package com.laundering.laundering_server.domain.member.model.dto.request;

public record SignUpRequest(
    String email,

    String password,

    String name,

    int stuNum,

    String roomNum
) {
}
