package com.example.devSns.app.domain.model;

import java.time.LocalDateTime;

public class Post {
    private final Long id;
    private final String content;
    private final String authorName;
    private final int likes;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Post(Long id, String content, String authorName, int likes,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.authorName = authorName;
        this.likes = likes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getContent() { return content; }
    public String getAuthorName() { return authorName; }
    public int getLikes() { return likes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public Post withContent(String newContent) {
        return new Post(id, newContent, authorName, likes, createdAt, updatedAt);
    }

    public Post withLikes(int newLikes) {
        return new Post(id, content, authorName, newLikes, createdAt, updatedAt);
    }
}
