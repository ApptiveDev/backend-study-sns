package com.example.devSns.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class LikeToggleRequest {

    @NotNull
    private Long memberId;

    @NotNull
    private Long postId;
}
