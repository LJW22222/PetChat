package com.chat.animal.persistence.user.entity;

import com.chat.animal.domain.user.User;
import com.chat.animal.domain.user.vo.OAuthProvider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    private String password;

    @Column(name = "provider_id", unique = true)
    private String providerId;

    @Enumerated(EnumType.STRING)
    private OAuthProvider provider;

    public UserEntity(String email, String nickname, String password, String providerId, OAuthProvider provider) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.providerId = providerId;
        this.provider = provider;
    }

    public void updateProviderInfo(String providerId, OAuthProvider oAuthProvider) {
        this.providerId = providerId;
        this.provider = oAuthProvider;
    }

    public static User toDomain(UserEntity userEntity) {
        return new User(userEntity.id, userEntity.nickname, userEntity.email, userEntity.password, userEntity.providerId, userEntity.provider);
    }

    public static UserEntity toEntity(User user) {
        return new UserEntity(user.getEmail(), user.getNickName(), user.getPassword(), user.getProviderId(), user.getProvider());
    }

}
