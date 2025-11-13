package com.example.devSns.dto;

import com.example.devSns.entities.Posts;
import com.example.devSns.entities.Replies;
import com.example.devSns.entities.Users;

import java.time.LocalDateTime;

public record ReplyDTO(
   Long userID,
   String comment
) {
    public static Replies dtoToEntity(Posts post, Users user, ReplyDTO replyDTO) {
        return Replies.builder()
                .posts(post)
                .users(user)
                .createAt(LocalDateTime.now())
                .reply(replyDTO.comment())
                .build();
    }
}
