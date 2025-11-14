package com.example.devSns.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class LikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    protected LikeEntity() {}

    public static LikeEntity create(MemberEntity member, PostEntity postEntity) {
        LikeEntity like = new LikeEntity();
        like.member = member;
        like.post = postEntity;

        // JPA 양방향 편의 메서드 -> 자동 갱신
        member.getLikes().add(like);
        postEntity.getLikes().add(like);
        return like;
    }
}
