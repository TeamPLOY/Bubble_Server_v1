package com.laundering.laundering_server.domain.member.model.dto.request;

public record DeleteRequest(
        String email,
        String password
) {
}
