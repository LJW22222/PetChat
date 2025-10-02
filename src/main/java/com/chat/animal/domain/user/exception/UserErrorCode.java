package com.chat.animal.domain.user.exception;

import com.chat.animal.common.exception.BaseErrorCode;
import com.chat.animal.common.exception.ErrorReason;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserErrorCode implements BaseErrorCode {

    USER_NOT_FOUND(404, "USER_ERROR", "사용자가 존재하지 않습니다.")
    ;

    private final Integer status;
    private final String errorCode;
    private final String message;

    UserErrorCode(int status, String errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status, errorCode, message);
    }

    public int status() {
        return status;
    }

    public String message() {
        return message;
    }
}
