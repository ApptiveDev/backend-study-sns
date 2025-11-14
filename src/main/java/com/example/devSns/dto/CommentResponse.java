package com.example.devSns.dto;

import com.example.devSns.entity.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        String username,
        LocalDateTime createdAt
){
    public CommentResponse(Comment comment){
        this(
                comment.getId(),
                comment.getContent(),
                comment.getUsername(),
                comment.getCreatedAt()
        );
    }
}
