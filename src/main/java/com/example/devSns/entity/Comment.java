package com.example.devSns.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String username;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name="post_id",
            nullable = false
    )
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @PrePersist
    public void onCreate(){
        createdAt = LocalDateTime.now();
    }
    public void update(String content){
        this.content = content;
    }
    public void assignTo(Post post){
        this.post = post;
    }
    public void assignMember(Member member){
        this.member = member;
        member.addComment(this);
    }
    public static Comment create(String content, Member member, Post post) {
        return Comment.builder()
                .content(content)
                .username(member.getUsername())
                .member(member)
                .post(post)
                .build();
    }
}