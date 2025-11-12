package com.example.devSns.dto.likes;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record LikesRequestDto(
        @NotNull @JsonProperty("target_id") Long targetId,
        @NotNull @JsonProperty("member_id") Long memberId
) {
}


