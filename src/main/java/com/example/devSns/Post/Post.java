package com.example.devSns.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Post {

    private Long id;

    private String content;
    private Long likeCount;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
