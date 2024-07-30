package com.laundering.laundering_server.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 입력 값입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없는 사용자입니다."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 데이터입니다."),
    DUPLICATED(HttpStatus.CONFLICT, "중복된 데이터입니다."),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류입니다."),
    APPLE_LOGIN_FAIL(HttpStatus.BAD_REQUEST, "애플 로그인 실패");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}