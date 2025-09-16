package com.chat.animal.persistence.user.repository;

import com.chat.animal.domain.user.vo.OAuthProvider;
import com.chat.animal.persistence.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByProviderIdAndProvider(String providerId, OAuthProvider provider);

//    List<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEmail(String email);
}
