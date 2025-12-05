package com.example.devSns.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class LikeResponse {
    @NotNull
    private Long PostId;
    private long likeCount;
    private boolean liked;
}
