package com.example.devSns.task.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 이 생성자는 JPA를 위해 존재, 외부에서 객체 생성을 막기 위해 protected로 설정.
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 값을 데이터베이스가 알아서 증가시키도록 맡김
    private Long id;

    @Column(length = 20, nullable = false, unique=true)
    private String username;

    @Column(length = 1000, nullable = false)
    private String postContent;

    private int likes;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true) // cascade = CascadeType.REMOVE 와 orphanRemoval의 차이??
    private final List<Comment> comments = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 생성자를 통해 필수 필드 초기화
    // Lombok에서 제공하는 객체를 안전하고 유연하게 생성할 수 있도록 도와주는 디자인 패턴
    @Builder
    public Post(String username, String postContent) {
        this.username = username;
        this.postContent = postContent;
        this.likes = 0; // 초기 좋아요 0
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 게시글 수정
    // 로직이지만 데이터의 일관성과 무결성을 지키기 위함 -> 잘 이해 안됨.
    public void update(String postContent) {
        this.postContent = postContent;
        this.updatedAt = LocalDateTime.now();
    }
}
