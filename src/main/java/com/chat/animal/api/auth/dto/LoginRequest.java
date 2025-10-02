package com.chat.animal.api.auth.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private final String email;
    private final String password;

}
