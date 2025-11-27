package com.example.devSns.dto;

import com.example.devSns.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponseDto {
    private String username;
    private String nickname;

    // Member 엔티티를 받아서 DTO로 변환해주는 정적 메서드
    public static MemberResponseDto of(Member member) {
        return MemberResponseDto.builder()
                .username(member.getUsername())
                .nickname(member.getNickname())
                .build();

    }
}