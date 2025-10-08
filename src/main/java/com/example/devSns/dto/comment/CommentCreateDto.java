package com.example.devSns.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CommentCreateDto(
        @NotNull Long post_id,
        @NotNull @NotEmpty String content,
        @NotNull @NotEmpty @NotBlank String user_name
        ) {
}
