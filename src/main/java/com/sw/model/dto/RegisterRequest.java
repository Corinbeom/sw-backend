package com.sw.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RegisterRequest {
    private String nickName;
    private String userEmail;
    private String password;
    private String verificationCode;
    private Date birthDate;
    private Date createdTime;
}
