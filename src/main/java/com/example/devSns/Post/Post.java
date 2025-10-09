package com.example.devSns.Post;

import com.example.devSns.Post.DTO.UpdatePostRequestDTO;
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

    private Long id;

    private String content;

    private Long likeCount;

    private String username;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public void Update(UpdatePostRequestDTO DTO){
        this.content = DTO.content();
        this.username = DTO.username();
        this.updatedAt = LocalDateTime.now();
    }

}
