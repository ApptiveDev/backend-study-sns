package com.example.devSns.dto;

import com.example.devSns.entities.Posts;
import com.example.devSns.entities.Users;
import lombok.*;

import java.time.LocalDateTime;

@Builder
public record PostResponse(
   Long id,
   String username,
   String content,
   Integer like,
   LocalDateTime createAt,
   LocalDateTime updateAt
) {
    public static PostResponse entityToDto(Posts post) {
        return PostResponse.builder()
                .id(post.getId())
                .username(post.getUsers().getUsername())
                .content(post.getContent())
                .like(post.getLikeit())
                .createAt(post.getCreateat())
                .updateAt(post.getUpdateat())
                .build();
    }
}
