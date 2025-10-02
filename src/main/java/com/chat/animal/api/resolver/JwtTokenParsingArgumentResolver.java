package com.chat.animal.api.resolver;

import com.chat.animal.api.annotation.JwtTokenParsing;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class JwtTokenParsingArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JwtTokenParsing.class)
                && String.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        JwtTokenParsing meta = parameter.getParameterAnnotation(JwtTokenParsing.class);
        if (meta == null) {
            throw new IllegalStateException("@JwtTokenParsing not present");
        }

        HttpServletRequest req = webRequest.getNativeRequest(HttpServletRequest.class);

        String raw = null;
        if (req != null && req.getCookies() != null) {
            for (Cookie c : req.getCookies()) {
                if (meta.cookieName().equals(c.getName())) {
                    raw = c.getValue();
                    break;
                }
            }
        }


        if (!StringUtils.hasText(raw)) {
            if (meta.required()) {
                throw new MissingRequestHeaderException(meta.cookieName(), parameter);
            } else {
                return null;
            }
        }

        String token = sanitize(raw, meta.stripBearerPrefix());
        if (!StringUtils.hasText(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Empty bearer token");
        }

        return sanitize(raw, meta.stripBearerPrefix());
    }

    private static String sanitize(String raw, boolean stripBearer) {
        String s = raw.trim();
        if (stripBearer && s.regionMatches(true, 0, "Bearer ", 0, 7)) s = s.substring(7).trim();
        if (s.length() >= 2 && s.startsWith("\"") && s.endsWith("\"")) {
            s = s.substring(1, s.length() - 1);
        }
        return s;
    }

}
