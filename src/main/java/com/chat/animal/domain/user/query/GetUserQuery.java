package com.chat.animal.domain.user.query;

import com.chat.animal.domain.user.User;
import com.chat.animal.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserQuery {

    private final UserRepository userRepository;

    public User handle(Long userId) {
        return userRepository.findByUserId(userId);
    }

}
