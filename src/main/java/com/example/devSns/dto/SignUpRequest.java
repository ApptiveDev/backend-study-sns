package com.example.devSns.dto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class SignUpRequest {
    @NotEmpty(message="username cannot be empty")
    private final String username;

    @NotEmpty(message="email cannot be empty")
    private final String email;

    @NotEmpty(message="password cannot be empty")
    private final String password;

    public SignUpRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
