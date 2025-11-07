package com.example.devSns.task.dto;

import com.example.devSns.task.entity.Comment;
import java.time.LocalDateTime;

public record CommentResponseDto (
        Long id,
        String comment,
        String author,
        LocalDateTime createAt,
        LocalDateTime updateAt,
        Long postId
) {
    public CommentResponseDto(Comment comment) {
        this(
                comment.getId(),
                comment.getComment(),
                comment.getAuthor(),
                comment.getCreatedAt(),
                comment.getModifiedAt(),
                comment.getPost().getId()
        );
    }
}