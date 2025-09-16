//package com.chat.animal.application.auth.usecase;
//
//
//import com.chat.animal.domain.auth.command.CreateTokenCommand;
//import com.chat.animal.domain.user.User;
//import com.chat.animal.domain.user.query.GetUserByProviderAndProviderIdQuery;
//import com.chat.animal.domain.user.vo.OAuthProvider;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//import java.util.concurrent.TimeUnit;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class OAuth2AuthenticationUseCase {
//
//    private final GetUserByProviderAndProviderIdQuery getUserByProviderAndProviderIdQuery;
//    private final CreateTokenCommand createTokenCommand;
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    private static final String OAUTH2_USER_PREFIX = "oauth2:temp:";
//    private static final long REDIS_TTL_MINUTES = 30;
//
//    @Value("${app.oauth2.success-url:https://site-navy-six-67.vercel.app/auth/success}")
//    private String successBaseUrl;
//
//    @Value("${app.oauth2.failure-url:https://site-navy-six-67.vercel.app/auth/nickname}")
//    private String failureUrl;
//
//    private String localSuccessBaseUrl = "http://localhost:5173/auth/success";
//
//    private String localFailureUrl = "http://localhost:5173/auth/nickname";
//
//    public String handleAuthenticationSuccess(OAuth2User oauth2User, String accessToken) {
//        // 1. OAuth2 정보 추출
//        String providerId = oauth2User.getAttribute("provider_id");
//        OAuthProvider providerType = oauth2User.getAttribute("provider_type");
//
//
//        // 2. 사용자 조회
//        Optional<User> userOptional = getUserByProviderAndProviderIdQuery.handle(providerId, providerType);
//
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            log.info("기존 사용자 찾음 - User ID: {}, Email: {}", user.getUserId(), user.getEmail());
//
//            // 3. 토큰 저장
//            createTokenCommand.generate(user.getUserId(), accessToken);
//
//            // 4. 환경에 따른 성공 URL 반환
//            String baseUrl = isLocalClient ? localSuccessBaseUrl : successBaseUrl;
//            return baseUrl + "?token=" + accessToken;
//
//        } else {
//            log.warn("사용자를 찾을 수 없음 - 새로운유저 생성  Provider ID: {}, Provider Type: {}", providerId, providerType);
//            // OAuth2User 속성들을 Redis에 저장
//            saveOAuth2UserToRedis(accessToken, oauth2User);
//
//            // 환경에 따른 실패 URL 반환
//            String baseUrl = isLocalClient ? localFailureUrl : failureUrl;
//            return baseUrl + "?token=" + accessToken;
//        }
//    }
//
//    private void saveOAuth2UserToRedis(String accessToken, OAuth2User oauth2User) {
//        String redisKey = OAUTH2_USER_PREFIX + accessToken;
//        Map<String, Object> attributes = new HashMap<>();
//
//        oauth2User.getAttributes().forEach((key, value) -> {
//            if (value != null) {
//                // provider_id로 저장
//                if (key.equals("id")) {
//                    attributes.put("provider_id", value.toString());
//                } else {
//                    attributes.put(key, value.toString());
//                }
//            }
//        });
//
//        try {
//            redisTemplate.opsForHash().putAll(redisKey, attributes);
//            redisTemplate.expire(redisKey, REDIS_TTL_MINUTES, TimeUnit.MINUTES);
//            log.info("OAuth2 사용자 정보 Redis 저장 완료 - Access Token: {}", accessToken);
//        } catch (Exception e) {
//            log.error("OAuth2 사용자 정보 Redis 저장 실패 - Access Token: {}, Error: {}", accessToken, e.getMessage());
//        }
//    }
//}
