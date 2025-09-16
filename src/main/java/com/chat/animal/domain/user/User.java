package com.chat.animal.domain.user;

import com.chat.animal.domain.user.vo.OAuthProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class User {

    private final Long userId;
    private final String nickName;
    private final String email;
    private final String password;
    private String providerId;
    private OAuthProvider provider;

    public User(Long userId, String nickName, String email, String password, String providerId, OAuthProvider provider) {
        this.userId = userId;
        this.nickName = nickName;
        this.email = email;
        this.password = password;
        this.providerId = providerId;
        this.provider = provider;
    }

    public void updateProviderInfo(String inProviderId, OAuthProvider inProvider) {
        providerId = inProviderId;
        provider = inProvider;
    }
}
