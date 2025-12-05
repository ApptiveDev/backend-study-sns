package com.example.devSns.dto;

import com.example.devSns.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
public class MemberJoinRequest {

    @NotEmpty(message="username cannot be empty")
    private String username;

    @NotEmpty(message="email cannot be empty")
    private String email;

    @NotEmpty(message="password cannot be empty")
    private String password;

    public Member toEntity(){
        return Member.create(username, email, password);
    }
}
