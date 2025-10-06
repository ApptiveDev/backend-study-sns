package com.example.devSns.dto.post;

import com.example.devSns.domain.Comment;
import com.example.devSns.domain.Post;
import com.example.devSns.dto.comment.CommentResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponseDto(
        Long id,
        String content,
        String user_name,
        LocalDateTime created_at,
        LocalDateTime updated_at,
        List<CommentResponseDto> comments
) {

    public static PostResponseDto from(Post post, List<Comment> comments) {
        return new PostResponseDto(
                post.getId(),
                post.getContent(),
                post.getUserName(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                comments.stream().map(CommentResponseDto::from).toList()
        );
    }
}
