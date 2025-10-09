package com.example.devSns.app.data.jpa.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@EntityListeners(AuditingEntityListener.class)
public class PostEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false, length = 100)
    private String authorName;

    @Column(nullable = false)
    private int likes = 0;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    protected PostEntity() { }

    public PostEntity(String content, String authorName) {
        this.content = content;
        this.authorName = authorName;
    }

    // getters/setters
    public Long getId() { return id; }
    public String getContent() { return content; }
    public String getAuthorName() { return authorName; }
    public int getLikes() { return likes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setContent(String content) { this.content = content; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public void like() { this.likes += 1; }
}
