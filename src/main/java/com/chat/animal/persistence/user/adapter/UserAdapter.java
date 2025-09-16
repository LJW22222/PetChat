package com.chat.animal.persistence.user.adapter;

import com.chat.animal.domain.user.User;
import com.chat.animal.domain.user.repository.UserRepository;
import com.chat.animal.domain.user.vo.OAuthProvider;
import com.chat.animal.persistence.user.entity.UserEntity;
import com.chat.animal.persistence.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> save(User user) {
        UserEntity userEntity = userJpaRepository.findById(user.getUserId()).map(getUserEntity ->
        {
            getUserEntity.updateProviderInfo(user.getProviderId(), user.getProvider());
            return getUserEntity;
        }).orElseGet(() -> UserEntity.toEntity(user));

        UserEntity saveUserEntity = userJpaRepository.save(userEntity);
        return Optional.of(UserEntity.toDomain(saveUserEntity));
    }

    @Override
    public User findByUserId(Long userId) {
        UserEntity userEntity = userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User Not Found with id - " + userId));
        return UserEntity.toDomain(userEntity);
    }

    @Override
    public Optional<User> findByProviderIdAndProvider(String providerId, OAuthProvider oAuthProvider) {
        Optional<UserEntity> byProviderIdAndProvider = userJpaRepository.findByProviderIdAndProvider(providerId, oAuthProvider);
        if (byProviderIdAndProvider.isPresent()) {
            UserEntity userEntity = byProviderIdAndProvider.get();
            return Optional.of(UserEntity.toDomain(userEntity));
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> userEntity = userJpaRepository.findByEmail(email);
        return userEntity.map(UserEntity::toDomain);
    }
}
