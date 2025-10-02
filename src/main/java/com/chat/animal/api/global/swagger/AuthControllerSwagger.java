package com.chat.animal.api.global.swagger;

import com.chat.animal.api.auth.dto.LoginRequest;
import com.chat.animal.api.auth.dto.UserRequest;
import com.chat.animal.api.global.dto.Response;
import com.chat.animal.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthControllerSwagger {

    @Operation(summary = "회원가입", description = "새로운 사용자 회원가입을 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = User.class))),
    })
    Response<User> signUp(@Valid @RequestBody UserRequest request);


//    @Operation(summary = "로그인", description = "로그인 API")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200",
//                    description = "로그인 성공"
//            ),
//    })
//    Response<Void> login(@Valid @RequestBody LoginRequest request);
}
