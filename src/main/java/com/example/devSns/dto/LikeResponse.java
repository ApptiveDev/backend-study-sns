package com.example.devSns.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeResponse {
    private Long PostId;
    private long likeCount;
    private boolean liked;
}
