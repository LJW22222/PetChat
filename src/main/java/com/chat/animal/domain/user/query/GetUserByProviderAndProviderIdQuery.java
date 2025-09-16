package com.chat.animal.domain.user.query;

import com.chat.animal.domain.user.User;
import com.chat.animal.domain.user.repository.UserRepository;
import com.chat.animal.domain.user.vo.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GetUserByProviderAndProviderIdQuery {

    private final UserRepository userRepository;

    public Optional<User> handle(String providerId, OAuthProvider oAuthProvider) {
        return userRepository.findByProviderIdAndProvider(providerId, oAuthProvider);
    }

}
