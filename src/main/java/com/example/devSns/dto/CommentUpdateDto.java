package com.example.devSns.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateDto {
    private String content;
    private Long memberId; // 수정을 요청한 사람의 ID (권한 확인용)
}