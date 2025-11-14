package com.example.devSns.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDto {
    private Long id;
    private String title;
    private String content;
    private Long memberId; // 작성자 ID 추가
}
