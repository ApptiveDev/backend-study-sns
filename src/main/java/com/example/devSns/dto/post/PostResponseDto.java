package com.example.devSns.dto.post;

import com.example.devSns.domain.Comment;
import com.example.devSns.domain.Post;
import com.example.devSns.dto.comment.CommentResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponseDto(
        @NotNull Long id,
        @NotNull @NotEmpty String content,
        @NotNull @NotBlank @JsonProperty("user_name") String userName,
        @NotNull @JsonProperty("like_count") Long likeCount,
        @NotNull @JsonProperty("created_at") LocalDateTime createdAt,
        @Nullable @JsonProperty("updated_at") LocalDateTime updatedAt,
        @Nullable List<CommentResponseDto> comments
) {

    public static PostResponseDto from(Post post, List<Comment> comments) {
        return new PostResponseDto(
                post.getId(),
                post.getContent(),
                post.getUserName(),
                post.getLikeCount(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                comments == null ?
                        List.of() : comments.stream().map(CommentResponseDto::from).toList()
        );
    }
}
