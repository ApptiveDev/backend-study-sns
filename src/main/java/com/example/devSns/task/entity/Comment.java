package com.example.devSns.task.entity;

import com.example.devSns.task.dto.CommentCreateDto;
import com.example.devSns.task.dto.CommentUpdateDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 이 생성자는 JPA를 위해 존재, 외부에서 객체 생성을 막기 위해 protected로 설정.
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Postgresql에서 SERIAL과 동일한 역할
    Long id;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private String author;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private Comment(String comment,String author, Post post){
        this.comment = comment;
        this.author = author;
        this.post = post;
    }

    public static Comment of(CommentCreateDto dto,Post post){
        if(dto.comment() == null || dto.comment().trim().isEmpty()){
            throw new IllegalArgumentException("댓글은 비워둘 수 없음");
        }
        return new Comment(dto.comment(),dto.author(), post);
    }

    public void update(CommentUpdateDto dto){
        if(dto.comment() != null) {
            if (dto.comment().trim().isEmpty()) throw new IllegalArgumentException("댓글은 비워둘 수 없음");
        }
        this.comment = dto.comment();
    }
}
