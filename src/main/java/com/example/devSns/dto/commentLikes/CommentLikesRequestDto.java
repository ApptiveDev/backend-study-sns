package com.example.devSns.dto.commentLikes;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record CommentLikesRequestDto(
        @NotNull @JsonProperty("comment_id") Long commentId,
        @NotNull @JsonProperty("member_id") Long memberId
) {
}

