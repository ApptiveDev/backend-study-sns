package com.example.devSns.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "likes",
        uniqueConstraints = @UniqueConstraint(columnNames ={"member_id","post_id"} )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id",nullable = false)
    private Post post;

    private LocalDateTime CreatedAt;

    private Like(Member member, Post post){
        this.member = member;
        this.post = post;
        this.CreatedAt = LocalDateTime.now();
    }

    public static Like create(Member member, Post post){
        Like like = new Like(member, post);
        member.addLike(like);
        return like;
    }

    public void assignMember(Member member) {
        this.member = member;
        member.addLike(this);
    }

    public void assignPost(Post post) {
        this.post = post;
        post.addLike(this);

    }

}
