package com.example.devSns.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(

        @NotBlank(message = "Not blank")
        @Size(max = 1000)
        String content,

        @NotBlank(message = "Not blank")
        String username,

        Long memberId
) {}
