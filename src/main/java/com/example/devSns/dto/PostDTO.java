package com.example.devSns.dto;

import com.example.devSns.entities.Posts;
import com.example.devSns.entities.Users;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record PostDTO(
    Long userId,
    @NotBlank String username,
    String content
) {
    public static Posts dtoToEntity(PostDTO postDTO, Users user) {
        Posts postEntity = Posts.builder()
                .users(user)
                .content(postDTO.content())
                .createat(LocalDateTime.now())
                .build();
        return postEntity;
    }
}
