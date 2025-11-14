package com.example.devSns.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeToggleRequest {
    private Long memberId;
    private Long postId;
}
