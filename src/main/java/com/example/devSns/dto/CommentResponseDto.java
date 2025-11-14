package com.example.devSns.dto;

import com.example.devSns.domain.Comment;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private String authorNickname; // 작성자 닉네임
    private List<CommentResponseDto> children;

    public static CommentResponseDto from(Comment comment) {
        String nickname = (comment.getMember() != null) ? comment.getMember().getNickname() : "Unknown";

        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                nickname, // 닉네임 전달
                comment.getChildren().stream()
                        .map(CommentResponseDto::from)
                        .collect(Collectors.toList())
        );
    }

    private CommentResponseDto(Long id, String content, String authorNickname, List<CommentResponseDto> children) {
        this.id = id;
        this.content = content;
        this.authorNickname = authorNickname;
        this.children = children;
    }
}