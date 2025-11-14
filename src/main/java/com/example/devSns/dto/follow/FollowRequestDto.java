package com.example.devSns.dto.follow;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record FollowRequestDto(
        @NotNull @JsonProperty("follower_id") Long followerId,
        @NotNull @JsonProperty("following_id") Long followingId
) {
}
