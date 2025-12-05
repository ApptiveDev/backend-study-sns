package com.example.devSns.dto;

import com.example.devSns.entity.Post;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class PostResponse {
    @NotNull
    private Long id;

    @NotNull
    private Long memberId;

    @NotEmpty(message="content cannot be empty")
    private String content;

    @NotEmpty(message="username cannot be empty")
    private String username;

    @NotNull
    private LocalDateTime createdAt;
    @NotNull
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
