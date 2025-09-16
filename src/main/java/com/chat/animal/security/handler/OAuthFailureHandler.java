package com.chat.animal.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class OAuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex) throws IOException {
        log.error("OAuth2 로그인 실패: {}", ex.getMessage(), ex);
        res.setStatus(400);
        res.setContentType("application/json;charset=UTF-8");

        String body = "{\"ok\":false,\"error\":\"" + ex.getClass().getSimpleName() + "\",\"message\":\"" + ex.getMessage() + "\"}";
        if (ex instanceof org.springframework.security.oauth2.core.OAuth2AuthenticationException oae) {
            var err = oae.getError();
            body = String.format("{\"ok\":false,\"error\":\"%s\",\"description\":\"%s\"}", err.getErrorCode(), String.valueOf(err.getDescription()));
        }
        res.getWriter().write(body);
    }
}
