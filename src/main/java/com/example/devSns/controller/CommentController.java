package com.example.devSns.controller;

import com.example.devSns.entity.Comment;
import com.example.devSns.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post/{postId}/comment")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<Comment> getComments(@PathVariable Long postId) {
        return commentService.getCommentByPost(postId);
    }

    @PostMapping
    public Comment createComment(@PathVariable Long postId, @RequestBody Comment comment) {
        return commentService.addComment(postId, comment);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Long commentId,
            @RequestBody Map<String, String> request
    ) {
        String newContent = request.get("content");
        Comment updated = commentService.updateComment(commentId, newContent);
        return ResponseEntity.ok(updated);
    }
}