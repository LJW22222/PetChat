package com.chat.animal.api.auth.aop;

import com.chat.animal.api.global.dto.Response;
import com.chat.animal.common.exception.PetChatException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerErrorHandler {

    @ExceptionHandler(PetChatException.class)
    public Response<Void> handleAiroException(PetChatException exception, HttpServletRequest request) {
        return Response.error(
                exception.getErrorCode().getErrorReason(),
                request.getRequestURI(),
                exception.getMessage()
        );
    }

}