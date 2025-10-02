package com.chat.animal.api.auth;

import com.chat.animal.api.auth.dto.LoginRequest;
import com.chat.animal.api.auth.dto.UserRequest;
import com.chat.animal.api.global.dto.Response;
import com.chat.animal.api.global.swagger.AuthControllerSwagger;
import com.chat.animal.application.user.usecase.UserUseCase;
import com.chat.animal.domain.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController implements AuthControllerSwagger {

    private final UserUseCase userUseCase;


    //로컬 로그인
    @Override
    @PostMapping("/sign-up")
    public Response<User> signUp(@Valid @RequestBody UserRequest request) {
        User user = userUseCase.createUser(request.getNickName(), request.getPassword(), request.getEmail());
        return Response.success(user);
    }

    //로그아웃
    @PostMapping("/logout")
    public Response<Void> logout(@Valid @RequestBody LoginRequest request) {
        userUseCase.login(request.getEmail(), request.getPassword());
        return Response.success();
    }

//    //이메일 중복 확인
//    @Override
//    @PostMapping("/check-email")
//    public Response<Void> login(@Valid @RequestBody LoginRequest request) {
//         userUseCase.login(request.getEmail(), request.getPassword());
//        return Response.success();
//    }
//
//    //닉네임 중복 확인
//    @Override
//    @PostMapping("/check-nickname")
//    public Response<Void> login(@Valid @RequestBody LoginRequest request) {
//        userUseCase.login(request.getEmail(), request.getPassword());
//        return Response.success();
//    }







}
