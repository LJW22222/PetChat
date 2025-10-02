package com.chat.animal.security.handler;

import com.chat.animal.common.jwt.JwtProvider;
import com.chat.animal.domain.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider; // access/refresh 생성기

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        Long userId = null;
        String email = null;

        System.out.println(authentication.getPrincipal().getClass());
        Object p = authentication.getPrincipal();
//        if (p instanceof MyUserDetails u) {              // 네가 쓰는 커스텀 UserDetails
//            userId = u.getId();
//            email  = u.getEmail();
//        } else if (p instanceof User su) { // 기본 User
//            email = su.getUsername();                   // username만 있음
//            // userId가 필요하면 username으로 DB 조회해서 가져오면 됨
//        }
//
//        if (userId == null && email == null) {
//            throw new IllegalStateException("No user identity on principal");
//        }
//
//        String access  = jwtProvider.generateAccessToken(userId, email, authentication.getAuthorities());
//        String refresh = jwtProvider.generateRefreshToken(userId, email);
//
//        // 3) 쿠키로 세팅 (FE/BE 분리 환경이면 SameSite=None + Secure=true)
//        boolean isProd = true; // 로컬(http) 테스트면 false로!
//        ResponseCookie accessCookie = ResponseCookie.from("access", access)
//                .httpOnly(true)
//                .secure(isProd)                    // 로컬 http면 false
//                .sameSite(isProd ? "None" : "Lax") // 도메인 다르면 None 필수
//                .path("/")
//                .maxAge(60 * 60)                   // 1h
//                .build();
//
//        ResponseCookie refreshCookie = ResponseCookie.from("refresh", refresh)
//                .httpOnly(true)
//                .secure(isProd)
//                .sameSite(isProd ? "None" : "Lax")
//                .path("/auth")                     // 갱신 엔드포인트 범위로 좁히기 권장
//                .maxAge(60L * 60 * 24 * 14)        // 14d
//                .build();
//
//        response.addHeader("Set-Cookie", accessCookie.toString());
//        response.addHeader("Set-Cookie", refreshCookie.toString());
//
//        // 4) 리다이렉트 (SPA로 보낼 거면 여기서)
//        getRedirectStrategy().sendRedirect(request, response, "https://petmatz-fe.vercel.app/");
    }
}