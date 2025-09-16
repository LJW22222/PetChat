package com.chat.animal.domain.user.command;

import com.chat.animal.domain.user.User;
import com.chat.animal.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class CreateUserCommand {

    private final UserRepository userRepository;

    public User handle(String nickName, String password, String email) {
        return userRepository.save(new User(0L, nickName, email, password, null, null)).orElseThrow(() ->
                new IllegalArgumentException("User Not Duplicate"));
    }

    public User handle(User user) {
        return userRepository.save(user).orElseThrow(() ->
                new IllegalArgumentException("User Not Duplicate"));
    }

}
