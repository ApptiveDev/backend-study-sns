package com.example.devSns.dto.comment;

import com.example.devSns.domain.Comment;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CommentResponseDto(
    @NotNull Long id,
    @NotNull Long post_id,
    @NotNull @NotBlank String content,
    @NotNull @NotBlank @NotEmpty String user_name,
    @NotNull  Long like_count,
    @NotNull  LocalDateTime created_at,
    @Nullable LocalDateTime updated_at
) {

    public static CommentResponseDto from(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getPostId(),
                comment.getContent(),
                comment.getUserName(),
                comment.getLikeCount(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
