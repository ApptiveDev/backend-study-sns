package com.example.devSns.task.entity;

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
    @Column(nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
