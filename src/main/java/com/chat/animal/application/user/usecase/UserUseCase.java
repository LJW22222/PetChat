package com.chat.animal.application.user.usecase;

import com.chat.animal.domain.user.User;
import com.chat.animal.domain.user.command.CreateUserCommand;
import com.chat.animal.domain.user.exception.UserErrorCode;
import com.chat.animal.domain.user.exception.UserException;
import com.chat.animal.domain.user.query.GetUserByEmailAndPasswordQuery;
import com.chat.animal.domain.user.query.GetUserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUseCase {

    private final CreateUserCommand createUserCommand;
    private final GetUserQuery getUserQuery;
    private final GetUserByEmailAndPasswordQuery getUserByEmailAndPasswordQuery;

    public User createUser(String nickName, String password, String email) {
        return createUserCommand.handle(nickName, password, email);
    }

    public void login(String email, String password) {
        if (!getUserByEmailAndPasswordQuery.handle(email, password)) {
            throw  UserException.notFound(UserErrorCode.USER_NOT_FOUND, "application");
        }
    }

    public User getUser(Long userId) {
        return getUserQuery.handle(userId);
    }
}
