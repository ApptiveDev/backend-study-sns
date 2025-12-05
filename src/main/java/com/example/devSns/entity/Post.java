package com.example.devSns.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String content;
    private String username;
    private LocalDateTime CreatedAt;
    private LocalDateTime UpdatedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @PrePersist
    public void onCreate(){
        CreatedAt = LocalDateTime.now();
        UpdatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        UpdatedAt = LocalDateTime.now();
    }
    public void update(String content){
        this.content = content;
    }
    public void AddComment(Comment comment){
        comments.add(comment);
        comment.AssignTo(this);
    }

    private Post(String content, Member member){
        this.content = content;
        this.member = member;
        this.CreatedAt = LocalDateTime.now();
        this.UpdatedAt = LocalDateTime.now();

        member.addPost(this);
    }

    public static Post create(String content, Member member){
        return new Post(content,member);
    }

    public void updateContent(String newContent){
        this.content = newContent;
        this.UpdatedAt = LocalDateTime.now();
    }

    public void addLike(Like like) {
        likes.add(like);
    }

}