package com.example.devSns.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "posts_likes")
public class PostLikes extends BaseLikeEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public PostLikes() {}
    public PostLikes(Member member, Post post) {
        super(member);
        this.post = post;
    }
}
