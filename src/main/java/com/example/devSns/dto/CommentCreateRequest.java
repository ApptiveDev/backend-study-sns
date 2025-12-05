package com.example.devSns.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class CommentCreateRequest {
    @NotEmpty(message= "Content cannot be empty")
    @Size(max = 500, message="Content cannot exceed 500 characters")
    private String content;

    @NotEmpty(message="Username cannot be empty")
    private String username;

    @NotNull
    private Long memberId;
}
