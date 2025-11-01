package com.example.devSns.Post;

import com.example.devSns.Post.Dto.UpdatePostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Post {

    private Long id; // 기본 키 생성 전략

    private String content;

    private Long likeCount;

    private String username;

    private LocalDateTime createdAt; // 생성 시점

    private LocalDateTime updatedAt; // 수정되는 시점에 변경

    public void Update(UpdatePostRequestDto Dto){
        this.content = Dto.content();
        this.userName = Dto.username();
        this.updatedAt = LocalDateTime.now();
    }

}
