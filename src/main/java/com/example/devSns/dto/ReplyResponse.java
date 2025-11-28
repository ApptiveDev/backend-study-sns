package com.example.devSns.dto;

import com.example.devSns.entities.Replies;
import lombok.Builder;

@Builder
public record ReplyResponse(
        long replyId,
        String username,
        String comment,
        Integer like
) {
    public static ReplyResponse entityToDTO(Replies replies) {
        return ReplyResponse.builder()
                .replyId(replies.getId())
                .username(replies.getUsers().getUsername())
                .comment(replies.getReply())
                .like(replies.getLikeit())
                .build();
    }
}