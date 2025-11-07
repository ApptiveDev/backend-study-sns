package com.example.devSns.task.dto;

import com.example.devSns.task.entity.Post;

import java.time.LocalDateTime;

// 게시글 응답 DTO
public record PostResponseDto(
        Long id,
        String username,
        String postContent,
        int likes,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {
    // Post 엔티티를 받아 DTO를 생성하는 생성자 (서비스 계층에서 사용)
    // record의 기본 생성자를 호출하여 값 초기화
    public PostResponseDto(Post post){
        this(
            post.getId(),
            post.getUsername(),
            post.getPostContent(),
            post.getLikes(),
            post.getCreatedAt(),
            post.getUpdatedAt()
        );
    }
}
