package com.example.devSns.dto;

import com.example.devSns.domain.Comment;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private List<CommentResponseDto> children; // 대댓글 목록

    // 엔티티를 DTO로 변환하는 정적 팩토리 메서드 (재귀 호출)
    public static CommentResponseDto from(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                // 자식 댓글(children)들도 재귀적으로 DTO로 변환
                comment.getChildren().stream()
                        .map(CommentResponseDto::from)
                        .collect(Collectors.toList())
        );
    }

    // private 생성자
    private CommentResponseDto(Long id, String content, List<CommentResponseDto> children) {
        this.id = id;
        this.content = content;
        this.children = children;
    }
}