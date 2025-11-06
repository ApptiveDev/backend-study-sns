package com.example.devSns.Post.Dto;

import com.example.devSns.Comment.Comment;

import java.time.LocalDateTime;
import java.util.List;

public record GetPostResponseDto(
        String content,
        Long likeCount,
        String username,
        LocalDateTime created_at,
        List<Comment> commnetList

)
{ }
