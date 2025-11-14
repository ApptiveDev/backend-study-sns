package com.example.devSns.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    private String username;
    private String content;
}

// Controller는 CommentEntity를 직접 받지 않고 CommentRequest만 받게 됨
