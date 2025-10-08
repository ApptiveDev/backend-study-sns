package com.example.devSns.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private int likes;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();
    @PrePersist
    public void onCreate(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        updatedAt = LocalDateTime.now();
    }
}