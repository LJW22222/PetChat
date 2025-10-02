package com.chat.animal.domain.user.exception;

import com.chat.animal.common.exception.BaseErrorCode;
import com.chat.animal.common.exception.PetChatException;

public class UserException extends PetChatException {


    public UserException(BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
    }

    public static UserException notFound(BaseErrorCode baseErrorCode, String sourceLayer) {
        return new UserException(baseErrorCode, sourceLayer);
    }
}
