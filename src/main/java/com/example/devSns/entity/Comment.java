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
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_diary_user_id_ref_user_id")
    )
    @JsonBackReference
    private Post post;

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
}