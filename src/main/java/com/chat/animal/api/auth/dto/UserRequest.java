package com.chat.animal.api.auth.dto;

import lombok.Data;

@Data
public class UserRequest {

    private final String nickName;
    private final String email;
    private final String password;

}
