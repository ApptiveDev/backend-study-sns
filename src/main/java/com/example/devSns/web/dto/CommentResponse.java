package com.example.devSns.web.dto;

import com.example.devSns.domain.Comment;

public record CommentResponse(
        Long id,
        String content
) {
    public static CommentResponse from(Comment c) {
        return new CommentResponse(c.getId(), c.getContent());
    }
}
