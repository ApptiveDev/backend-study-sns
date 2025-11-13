package com.example.devSns.dto;

import lombok.Getter;

@Getter
public class CommentResponse {
    private final Long id;
    private final String username;
    private final String content;
    private final String createdAt;

    public CommentResponse(Long id, String username, String content, String createdAt) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
    }
}

// Entity를 외부로 그대로 내보내지 않고 DTO로 가공해서 반환
