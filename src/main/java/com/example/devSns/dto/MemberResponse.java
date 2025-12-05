package com.example.devSns.dto;

import com.example.devSns.entity.Member;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class MemberResponse {

    @NotEmpty(message="id cannot be empty")
    private Long id;

    @NotEmpty(message="username cannot be empty")
    private String username;

    @NotEmpty(message="email cannot be empty")
    private String email;

    public MemberResponse(Member member){
        this.id = member.getId();
        this.username = member.getUsername();
        this.email = member.getEmail();
    }
}
