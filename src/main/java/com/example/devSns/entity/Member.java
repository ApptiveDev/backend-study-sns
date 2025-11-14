package com.example.devSns.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Like> likes = new ArrayList<>();

    private Member (String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public static Member create(String username, String email, String password){
        return new Member(username, email, password);
    }

    public void addPost(Post post) {
        posts.add(post);
    }
    public void addComment(Comment comment) {
        comments.add(comment);
    }
    public void addLike(Like like) {
        likes.add(like);
    }
}
