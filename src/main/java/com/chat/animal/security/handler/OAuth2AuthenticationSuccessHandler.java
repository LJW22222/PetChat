//package com.chat.animal.security.handler;
//
//import com.chat.animal.application.auth.usecase.OAuth2AuthenticationUseCase;
//import com.chat.animal.security.vo.SocialProfile;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//
//    private final OAuth2AuthenticationUseCase oauth2AuthenticationUseCase;
//    private final OAuth2AuthorizedClientService authorizedClientService;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                        Authentication authentication) throws IOException {
//        SocialProfile socialProfile = (SocialProfile) authentication.getPrincipal();
//
//        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
//        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
//                authToken.getAuthorizedClientRegistrationId(),
//                authToken.getName());
//
//        String accessToken = client.getAccessToken().getTokenValue();
//        log.info("액세스 토큰: {}", accessToken);
//
//        try {
//            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
//
//
//            // UseCase로 인증 처리 위임 (환경 정보 전달)
//            String redirectUrl = oauth2AuthenticationUseCase.handleAuthenticationSuccess(oauth2User, accessToken);
//
//            response.sendRedirect(redirectUrl);
//
//
//        } catch (Exception e) {
//            log.error("OAuth2 인증 성공 처리 중 오류 발생", e);
//            response.sendRedirect("/auth/failure");
//        }
//    }
//}