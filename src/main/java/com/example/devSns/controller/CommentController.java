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
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 1. 댓글 생성
    @PostMapping("/comments")
    public CommentResponseDto createComment(@RequestBody CommentRequestDto requestDto) {
        Comment comment = commentService.createComment(requestDto);
        return CommentResponseDto.from(comment);
    }

    // 2. 특정 게시글의 모든 댓글/대댓글 조회
    @GetMapping("/posts/{postId}/comments")
    public List<CommentResponseDto> getComments(@PathVariable Long postId) {
        return commentService.getCommentsByPost(postId);
    }

    // 3. 댓글 수정
    @PutMapping("/comments/{id}")
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentUpdateDto updateDto) {
        // DTO에 memberId가 포함되어 서비스단에서 권한 확인
        Comment updatedComment = commentService.updateComment(id, updateDto);
        return CommentResponseDto.from(updatedComment);
    }

    // 4. 댓글 삭제
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, @RequestParam Long memberId) {
        // 삭제는 memberId를 파라미터로 받아 권한 확인
        commentService.deleteComment(id, memberId);
        return ResponseEntity.ok().build();
    }
}