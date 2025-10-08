package com.example.devSns.domain;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Post {
    private Long id;
    private String content;
    private Long likeCount;
    private String userName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Map<String, Object> toMap() {
        return Map.of(
                "id", id,
                "content", content,
                "like_count", likeCount,
                "user_name", userName,
                "created_at", createdAt,
                "updated_at", updatedAt
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
