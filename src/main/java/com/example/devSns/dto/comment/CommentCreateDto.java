package com.example.devSns.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CommentCreateDto(
        @NotNull @JsonProperty("post_id") Long postId,
        @NotNull @JsonProperty("member_id") Long memberId,
        @NotEmpty String content
        ) {
}
