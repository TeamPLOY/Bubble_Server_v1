package com.laundering.launering_server.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;  // 비즈니스 로직에서 발생한 에러를 식별하기 위한 에러 코드

    // 생성자: BusinessException 객체를 생성할 때 에러 코드를 인자로 받습니다.
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());  // RuntimeException의 생성자에 에러 메시지를 전달하여 예외 메시지를 설정합니다.
        this.errorCode = errorCode;  // 전달받은 에러 코드를 필드에 저장합니다.
    }
}

