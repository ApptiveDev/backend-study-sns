package com.example.devSns.Post.DTO;

import java.time.LocalDateTime;

public record GetPostResponseDTO(
        String content,
        Long likeCount,
        String username,
        LocalDateTime created_at

)
{ }
