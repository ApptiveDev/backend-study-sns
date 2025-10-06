package com.example.devSns.dto.comment;

import com.example.devSns.domain.Comment;

import java.time.LocalDateTime;

public record CommentResponseDto(
    Long id,
    Long post_id,
    String content,
    String user_name,
    LocalDateTime created_at,
    LocalDateTime updated_at
) {

    public static CommentResponseDto from(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getPostId(),
                comment.getContent(),
                comment.getUserName(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
