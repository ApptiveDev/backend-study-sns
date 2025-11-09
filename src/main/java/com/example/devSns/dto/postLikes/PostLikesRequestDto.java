package com.example.devSns.dto.postLikes;

import jakarta.validation.constraints.NotNull;

public record PostLikesRequestDto(
        @NotNull Long postId,
        @NotNull Long memberId
) {
}
