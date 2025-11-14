package com.example.devSns.dto;

import com.example.devSns.entity.Member;
import lombok.Getter;

@Getter
public class MemberResponse {
    private Long id;
    private String username;
    private String email;

    public MemberResponse(Member member){
        this.id = member.getId();
        this.username = member.getUsername();
        this.email = member.getEmail();
    }
}
