package com.example.devSns.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private String content;
    private Long postId;
    private Long parentId; // 대댓글인 경우 부모 댓글의 ID, 아니면 null
}