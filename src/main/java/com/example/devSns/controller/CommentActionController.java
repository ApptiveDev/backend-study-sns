package com.example.devSns.controller;

import com.example.devSns.dto.CommentResponse;
import com.example.devSns.dto.CommentUpdateRequest;
import com.example.devSns.entity.Comment;
import com.example.devSns.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentActionController {
    private final CommentService CommentService;

    public CommentActionController(CommentService commentService){
        this.CommentService = commentService;
    }
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        CommentService.deleteComment(commentId);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequest request
    ) {
        Comment updated = CommentService.UpdateComment(commentId, request.getContent());
        return ResponseEntity.ok(new CommentResponse(updated));
    }
}
