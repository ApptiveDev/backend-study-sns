package com.example.devSns.dto;

import com.example.devSns.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {

    private Long id;
    private Long memberId;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int likeCount;
    private int commentCount;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.memberId = post.getMember() != null ? post.getMember().getId() : null;
        this.content = post.getContent();
        this.username = post.getUsername();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.likeCount = post.getLikes().size();     // 리스트 -> size()
        this.commentCount = post.getComments().size();
    }
}
