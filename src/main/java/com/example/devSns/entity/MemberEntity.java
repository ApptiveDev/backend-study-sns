package com.example.devSns.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "member")
    private List<PostEntity> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<CommentEntity> comments = new ArrayList<>();

    protected MemberEntity() {}

    public static MemberEntity create(String username, String email) {
        MemberEntity member = new MemberEntity();
        member.username = username;
        member.email = email;
        member.createdAt = LocalDateTime.now();
        return member;
    }
}
