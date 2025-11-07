package com.example.devSns.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentCreateDto(
        @NotBlank(message = "댓글 내용은 필수입니다.")
        @Size(max = 1000, message = "내용은 1000자를 초과할 수 없습니다.")
        String comment,

        @NotBlank(message = "댓글 작성자명은 필수입니다.")
        String author
) {
}
