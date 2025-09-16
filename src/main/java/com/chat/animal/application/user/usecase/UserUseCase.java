package com.chat.animal.application.user.usecase;

import com.chat.animal.domain.user.User;
import com.chat.animal.domain.user.command.CreateUserCommand;
import com.chat.animal.domain.user.query.GetUserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUseCase {

    private final CreateUserCommand createUserCommand;
    private final GetUserQuery getUserQuery;

    public User createUser(String nickName, String password, String email) {
        return createUserCommand.handle(nickName, password, email);
    }

    public User getUser(Long userId) {
        return getUserQuery.handle(userId);
    }
}
