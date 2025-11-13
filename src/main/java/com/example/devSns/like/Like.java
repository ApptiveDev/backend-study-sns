package com.example.devSns.like;

import com.example.devSns.comment.Comment;

import com.example.devSns.member.Member;
import jakarta.persistence.*;

@Entity
@Table(name = "likes")

public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;

    protected Like() {}


    public Like(Member member, Comment comment) {
        this.member = member;
        this.comment = comment;
    }

    public Member getMember() { return member; }
    public Comment getComment() { return comment; }

    public Long getId() {return id;}
}
