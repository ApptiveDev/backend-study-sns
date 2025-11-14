package com.example.devSns.dto.member;

import com.example.devSns.domain.Member;

public record MemberResponseDto(
        Long id,
        String nickname
) {
    public static MemberResponseDto from(Member member) {
        return new MemberResponseDto(
                member.getId(),
                member.getNickname()
        );
    }
}
