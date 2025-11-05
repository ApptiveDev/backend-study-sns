package com.example.devSns.dto.comment;

import com.example.devSns.domain.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CommentResponseDto(
    @NotNull Long id,
    @NotNull @JsonProperty("post_id") Long postId,
    @NotNull @NotBlank String content,
    @NotNull @NotBlank @JsonProperty("user_name") String userName,
    @NotNull @JsonProperty("like_count") Long likeCount,
    @NotNull @JsonProperty("created_at") LocalDateTime createdAt,
    @Nullable @JsonProperty("updated_at") LocalDateTime updatedAt
) {

    public static CommentResponseDto from(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getPostId(),
                comment.getContent(),
                comment.getUserName(),
                comment.getLikeCount(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
