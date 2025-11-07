package com.example.devSns.controller;

import com.example.devSns.domain.Comment;
import com.example.devSns.dto.CommentRequestDto;
import com.example.devSns.dto.CommentResponseDto;
import com.example.devSns.dto.CommentUpdateDto;
import com.example.devSns.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") // /api/posts 와 /api/comments 로 분리
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 1. 댓글 생성 (POST /api/comments)
    // (대댓글이든 일반 댓글이든 이 API 하나로 처리)
    @PostMapping("/comments")
    public CommentResponseDto createComment(@RequestBody CommentRequestDto requestDto) {
        Comment comment = commentService.createComment(requestDto);
        // 생성된 댓글이 대댓글일 수 있으므로, 재귀 DTO 변환을 피하기 위해
        // 간단히 id와 content만 반환하거나, from을 쓰되 1레벨만 반환하도록 할 수 있습니다.
        // 여기서는 간단하게 from을 사용합니다.
        return CommentResponseDto.from(comment);
    }

    // 2. 특정 게시글의 모든 댓글/대댓글 조회 (GET /api/posts/{postId}/comments)
    @GetMapping("/posts/{postId}/comments")
    public List<CommentResponseDto> getComments(@PathVariable Long postId) {
        return commentService.getCommentsByPost(postId);
    }

    // 3. 댓글 수정 (PUT /api/comments/{id})
    @PutMapping("/comments/{id}")
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentUpdateDto updateDto) {
        Comment updatedComment = commentService.updateComment(id, updateDto.getContent());
        return CommentResponseDto.from(updatedComment);
    }

    // 4. 댓글 삭제 (DELETE /api/comments/{id})
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }
}