package com.chat.animal.domain.user.query;

import com.chat.animal.domain.user.User;
import com.chat.animal.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GetUserByEmailQuery {

    private final UserRepository userRepository;

    public Optional<User> handle(String email) {
        return userRepository.findByEmail(email);
    }

}
