package com.example.devSns.comment.dto;

import com.example.devSns.comment.Comment;
import java.time.LocalDateTime;

public class CommentResponse {
    private Long id;
    private String content;
    private String username;
    private LocalDateTime createdAt;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.username = comment.getUsername();
        this.createdAt = comment.getCreatedAt();
    }

    public Long getId() { return id; }
    public String getContent() { return content; }
    public String getUsername() { return username; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
