package com.sw.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private String userEmail;
    private String role;

    public LoginResponse(String token, String userEmail, String role) {
        this.token = token;
        this.userEmail = userEmail;
        this.role = role;
    }
}
