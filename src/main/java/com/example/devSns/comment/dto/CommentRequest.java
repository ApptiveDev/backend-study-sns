package com.example.devSns.comment.dto;

public class CommentRequest {
    private String content;
    private String username;

    public CommentRequest() {}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {   // ← 이거 추가
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) { // ← 이거 추가
        this.username = username;
    }
}
