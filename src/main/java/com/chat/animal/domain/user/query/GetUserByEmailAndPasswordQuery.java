package com.chat.animal.domain.user.query;

import com.chat.animal.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserByEmailAndPasswordQuery {

    private final UserRepository userRepository;

    public boolean handle(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
}
