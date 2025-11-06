package com.example.devSns.dto;

import com.example.devSns.entities.Posts;
import com.example.devSns.entities.Users;

public record PostDTO(
    Long userId,
    String username,
    String content
) {
    public static Posts dtoToEntity(PostDTO postDTO, Users user) {
        Posts postEntity = Posts.builder()
                .users(user)
                .content(postDTO.content())
                .build();
        return postEntity;
    }
}
