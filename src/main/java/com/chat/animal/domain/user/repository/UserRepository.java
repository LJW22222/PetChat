package com.chat.animal.domain.user.repository;

import com.chat.animal.domain.user.User;
import com.chat.animal.domain.user.vo.OAuthProvider;

import java.util.Optional;

public interface UserRepository {

    Optional<User> save(User user);

    User findByUserId(Long userId);

    Optional<User> findByProviderIdAndProvider(String providerId, OAuthProvider oAuthProvider);

    Optional<User> findByEmail(String email);
}
