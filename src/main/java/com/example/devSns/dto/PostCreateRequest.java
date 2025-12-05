package com.example.devSns.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
public class PostCreateRequest {
    @NotEmpty(message="content cannot be empty")
    private String content;

    @NotEmpty(message="memberId cannot be empty")
    private Long memberId;
}
