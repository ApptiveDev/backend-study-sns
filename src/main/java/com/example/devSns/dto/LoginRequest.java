package com.example.devSns.dto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class LoginRequest {

    @NotEmpty(message="email cannot be empty")
    private final String email;
    @NotEmpty(message="password cannot be empty")
    private final String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
