package com.example.devSns.dto;

import com.example.devSns.entity.Comment;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CommentResponse(
        @NotNull(message="id cannot be null")
        Long id,

        @NotEmpty(message= "Content cannot be empty")
        String content,

        @NotEmpty(message= "Username cannot be empty")
        String username,

        @NotNull
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
