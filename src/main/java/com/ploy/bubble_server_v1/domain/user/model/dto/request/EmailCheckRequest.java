package com.ploy.bubble_server_v1.domain.user.model.dto.request;

public record EmailCheckRequest(
    int code,
    String email
) {
}
