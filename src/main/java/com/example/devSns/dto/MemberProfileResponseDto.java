package com.example.devSns.dto;

import com.example.devSns.domain.Member;
import lombok.Getter;

import java.util.List;

@Getter
public class MemberProfileResponseDto {
    private Long id;
    private String nickname;
    private List<PostResponseDto> posts; // 내가 쓴 글
    private List<CommentResponseDto> comments; // 내가 쓴 댓글
    private List<PostResponseDto> likedPosts; // 내가 좋아요 한 글

    public MemberProfileResponseDto(Member member, List<PostResponseDto> posts, List<CommentResponseDto> comments, List<PostResponseDto> likedPosts) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.posts = posts;
        this.comments = comments;
        this.likedPosts = likedPosts;
    }
}