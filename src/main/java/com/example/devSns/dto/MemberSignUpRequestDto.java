package com.example.devSns.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSignUpRequestDto {
    private String username;
    private String password;
    private String nickname;
}