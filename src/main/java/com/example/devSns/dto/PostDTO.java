package com.example.devSns.dto;

import com.example.devSns.entities.Posts;

public record PostDTO(
    String username,
    String content
) {
    public static Posts dtoToEntity(PostDTO postDTO) {
        Posts postEntity = Posts.builder()
                .username(postDTO.username())
                .content(postDTO.content())
                .build();
        return postEntity;
    }
}
