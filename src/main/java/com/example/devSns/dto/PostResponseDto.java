package com.example.devSns.dto;

import com.example.devSns.domain.Post;
import lombok.Getter;

@Getter
public class PostResponseDto {
    private final Long id;
    private final String title;
    private final String content;
    private final String authorNickname; // 작성자 닉네임
    private final int likeCount; // 좋아요 수


    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        // Member가 null일 경우를 대비한 방어 코드
        this.authorNickname = (post.getMember() != null) ? post.getMember().getNickname() : "Unknown";
        this.likeCount = (post.getLikes() != null) ? post.getLikes().size() : 0;
    }
}