package com.example.devSns.dto;

public record ReplyResponse(
        long replyId,
        String title,
        String username,
        String comment
) {}