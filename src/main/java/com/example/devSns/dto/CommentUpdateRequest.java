package com.example.devSns.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class CommentUpdateRequest {
    @NotNull(message="Content cannot be null")
    private String content;
}
