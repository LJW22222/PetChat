package com.chat.animal.api.auth;

import com.chat.animal.api.annotation.JwtTokenParsing;
import com.chat.animal.api.annotation.UserPrincipal;
import com.chat.animal.api.auth.dto.UserRequest;
import com.chat.animal.application.user.usecase.UserUseCase;
import com.chat.animal.domain.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserUseCase userUseCase;

    @PostMapping("/sign-up")
    public ResponseEntity<User> signUp(@Valid @RequestBody UserRequest request) {
        User user = userUseCase.createUser(request.getNickName(), request.getPassword(), request.getEmail());
        return ResponseEntity.ok(user);
    }

//    /**
//     * 로그아웃
//     */
//    @PostMapping("/logout")
//    public ResponseEntity<Void> logout(
//            @UserPrincipal User user,
//            @JwtTokenParsing String accessToken) {
//        logoutCommand.execute(accessToken, user.getId());
//
//        return ResponseEntity.ok().build();
//    }


    private final StringRedisTemplate stringRedis;

    @GetMapping("/dev/success")
    public ResponseEntity<String> success(@RequestParam String key) {
        Map<Object, Object> all = stringRedis.opsForHash().entries("oauth2:exchange:" + key);
        // 여기서 필요한 값( accessToken, refreshToken, userId 등) 확인
        return ResponseEntity.ok("SUCCESS key=" + key + " data=" + all);
    }

    @GetMapping("/dev/signup")
    public ResponseEntity<String> signup(@RequestParam String key) {
        Map<Object, Object> all = stringRedis.opsForHash().entries("oauth2:exchange:" + key);
        return ResponseEntity.ok("SIGNUP key=" + key + " data=" + all);
    }

}
