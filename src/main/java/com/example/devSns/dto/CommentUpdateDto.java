package com.example.devSns.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateDto {
    private String content;
}
// 수정 시에는 content만 변경 가능하도록