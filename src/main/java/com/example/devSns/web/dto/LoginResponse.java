package com.example.devSns.web.dto;

import com.example.devSns.domain.Member;

public record LoginResponse(
        String accessToken,
        String tokenType,
        String refreshToken,
        Long memberId,
        String username
) {
    public static LoginResponse of(String accessToken, String refreshToken, Member member) {
        return new LoginResponse(
                accessToken,
                "Bearer",
                refreshToken,
                member.getId(),
                member.getUsername()
        );
    }
}
