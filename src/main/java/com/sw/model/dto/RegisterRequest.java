package com.sw.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String userId;
    private String nickName;
    private String userEmail;
    private String password;
    private String verificationCode;
}
