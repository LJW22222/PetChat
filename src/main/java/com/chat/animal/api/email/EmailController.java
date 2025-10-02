package com.chat.animal.api.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/email")
@RequiredArgsConstructor
@Slf4j
public class EmailController {

//    //이메일 인증 코드 발송
//    @Override
//    @PostMapping("/send-verification-code")
//    public Response<Void> login(@Valid @RequestBody LoginRequest request) {
//        userUseCase.login(request.getEmail(), request.getPassword());
//        return Response.success();
//    }



}
