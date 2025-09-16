package com.chat.animal.security.handler;

import com.chat.animal.common.jwt.JwtProvider;
import com.chat.animal.domain.user.User;
import com.chat.animal.domain.user.query.GetUserByProviderAndProviderIdQuery;
import com.chat.animal.domain.user.vo.OAuthProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final GetUserByProviderAndProviderIdQuery getUserByProviderAndProviderIdQuery;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken auth = (OAuth2AuthenticationToken) authentication;
        OAuth2User principal = auth.getPrincipal();



        String registrationId = auth.getAuthorizedClientRegistrationId(); // "kakao", "google", "naver" 등
        OAuthProvider provider = OAuthProvider.valueOf(registrationId.toUpperCase()); // enum 매핑

        String providerId = principal.getAttribute("provider_id");

        User user = getUserByProviderAndProviderIdQuery.handle(providerId, provider)
                .orElseThrow(() -> new IllegalStateException("사용자 없음"));

        String jwt = jwtProvider.generateAccessToken(user.getUserId());

        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .secure(true)     // 로컬 http면 false
                .sameSite("None") // 크로스 도메인일 때
                .path("/")
                .maxAge(3600)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        String redirectUrl = (user.getUserId() == null)
                ? "https://petmatz-fe.vercel.app/kakao-signup"
                : "https://petmatz-fe.vercel.app/kakao-login";

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }


}