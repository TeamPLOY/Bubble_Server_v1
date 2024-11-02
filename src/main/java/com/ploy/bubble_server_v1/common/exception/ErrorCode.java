package com.ploy.bubble_server_v1.common.exception;

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
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT,"이미 사용 중인 이메일입니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND,"찾을 수 없는 이메일 입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST,"비밀번호가 일치하지 않습니다."),
    RESERVATION_ALREADY_EXISTS(HttpStatus.CONFLICT,"해당 일에 예약이 이미 존재합니다."),
    RESERVATION_ALREADY_EXISTS_THIS_WEEK(HttpStatus.CONFLICT,"해당 주에 이미 에약을 했습니다.");


    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}