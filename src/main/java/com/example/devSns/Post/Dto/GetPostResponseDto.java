package com.example.devSns.Post.Dto;

import java.time.LocalDateTime;

public record GetPostResponseDto(
        String content,
        Long likeCount,
        String username,
        LocalDateTime created_at

)
{ }
