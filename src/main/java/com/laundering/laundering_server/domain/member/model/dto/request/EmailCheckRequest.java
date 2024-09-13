package com.laundering.laundering_server.domain.member.model.dto.request;

public record EmailCheckRequest(
    int code,
    String email
) {
}
