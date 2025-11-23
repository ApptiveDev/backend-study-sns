package com.example.devSns.dto.member;

import jakarta.validation.constraints.NotBlank;

public record MemberCreateDto(
    @NotBlank String nickname
) {
}
