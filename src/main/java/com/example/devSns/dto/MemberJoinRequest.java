package com.example.devSns.dto;

import com.example.devSns.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberJoinRequest {
    private String username;
    private String email;
    private String password;

    public Member toEntity(){
        return Member.create(username, email, password);
    }
}
