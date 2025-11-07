package com.example.devSns.web.dto;

import com.example.devSns.domain.Post;

public record PostResponse(
        Long id,
        String title,
        String content
) {
    public static PostResponse from(Post p) {
        return new PostResponse(p.getId(), p.getTitle(), p.getContent());
    }
}
