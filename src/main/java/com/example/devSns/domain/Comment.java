package com.example.devSns.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Post post;

    @Builder
    private Comment(String content, Post post) {
        this.content = content;
        this.post = post;
    }

    public void update(String content) {
        this.content = content;
    }

    /** Post 편의 메서드에서만 설정하도록 제한 */
    void setPostInternal(Post post) {
        this.post = post;
    }
}
