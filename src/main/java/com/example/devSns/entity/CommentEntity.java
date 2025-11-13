package com.example.devSns.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

// DTO를 사용하고 Setter를 삭제 (안전)
@Entity
@Getter
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String username;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    // Post와 N:1 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity postEntity;

    // JPA 기본 생성자
    protected CommentEntity() {}

    // 정적 생성 메서드 > 엔터티 변경은 오직 메서드로
    public static CommentEntity create(PostEntity postEntity, String username, String content) {
        CommentEntity comment = new CommentEntity();
        comment.postEntity = postEntity;
        comment.username = username;
        comment.content = content;
        return comment;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
