package com.chat.animal.security.service;

import com.chat.animal.domain.user.User;
import com.chat.animal.domain.user.command.CreateUserCommand;
import com.chat.animal.domain.user.query.GetUserByEmailQuery;
import com.chat.animal.domain.user.query.GetUserByProviderAndProviderIdQuery;
import com.chat.animal.domain.user.vo.OAuthProvider;
import com.chat.animal.security.vo.SocialProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2Service extends DefaultOAuth2UserService {

    private final GetUserByEmailQuery getUserByEmailQuery;
    private final CreateUserCommand createUserCommand;
    private final GetUserByProviderAndProviderIdQuery getUserByProviderAndProviderIdQuery;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        try {
            OAuth2User oAuth2User2 = super.loadUser(req);
        } catch (Exception e) {
            log.error("loadUser 예외: {} - {}", e.getClass().getName(), e.getMessage());
            throw e;
        }
        OAuth2User oAuth2User = super.loadUser(req);

        String regId = req.getClientRegistration().getRegistrationId(); // google/kakao/naver
        Map<String, Object> attrs = oAuth2User.getAttributes();
        OAuthProvider provider = switch (regId) {
            case "google" -> OAuthProvider.GOOGLE;
            case "kakao"  -> OAuthProvider.KAKAO;
            case "naver"  -> OAuthProvider.NAVER;
            default -> throw new IllegalArgumentException("Unsupported provider: " + regId);
        };

        // 1) 정규화
        SocialProfile p = mapToProfile(provider, attrs);

        // 2) upsert(가입/갱신)
        User user = upsertUser(provider, p);

        // 3) Security 주체 생성 (권한 최소 ROLE_USER)
        Collection<GrantedAuthority> auth = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        Map<String,Object> principalAttrs = Map.of("userId", user.getUserId(), "provider", provider.name(), "providerId", p.id());
        return new DefaultOAuth2User(auth, principalAttrs, "userId");
    }

    private SocialProfile mapToProfile(OAuthProvider provider, Map<String,Object> a) {
        switch (provider) {
            case GOOGLE -> {
                return new SocialProfile(
                        (String) a.get("sub"),
                        (String) a.get("email"),
                        (String) a.getOrDefault("name", ""),
                        (String) a.getOrDefault("picture", "")
                );
            }
            case KAKAO -> {
                Long id = ((Number) a.get("id")).longValue();
                Map<String,Object> account = (Map<String,Object>) a.get("kakao_account");
                String email = account != null ? (String) account.get("email") : null;
                Map<String,Object> profile = account != null ? (Map<String,Object>) account.get("profile") : null;
                String nickname = profile != null ? (String) profile.getOrDefault("nickname","") : "";
                String imageUrl = profile != null ? (String) profile.getOrDefault("profile_image_url","") : "";
                return new SocialProfile(String.valueOf(id), email, nickname, imageUrl);
            }
            case NAVER -> {
                Map<String,Object> res = (Map<String,Object>) a.get("response");
                return new SocialProfile(
                        (String) res.get("id"),
                        (String) res.get("email"),
                        (String) res.getOrDefault("name",""),
                        (String) res.getOrDefault("profile_image","")
                );
            }
        }
        throw new IllegalStateException();
    }

    private User upsertUser(OAuthProvider provider, SocialProfile p) {
        // (1) provider+providerId 우선 조회
        User user = getUserByProviderAndProviderIdQuery.handle(p.id(), provider)
                .orElseGet(() -> {
                    // (2) 없으면 email로 매칭(동일 이메일 소셜 연결)하거나 새로 생성
                    return getUserByEmailQuery.handle(p.email()).orElseGet(() ->
                            new User(0L, p.name(), p.email(), null, p.id() ,provider));
                });

        user.updateProviderInfo(p.id(), provider);

        return createUserCommand.handle(user);
    }
}

