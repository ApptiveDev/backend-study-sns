package com.example.devSns.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private String content;
    private Long postId;
    private Long parentId;
    private Long memberId; // 작성자 ID 추가
}