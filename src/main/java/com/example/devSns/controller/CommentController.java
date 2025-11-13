package com.example.devSns.controller;

import com.example.devSns.dto.CommentRequest;
import com.example.devSns.entity.CommentEntity;
import com.example.devSns.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 생성
    @PostMapping("/{postId}")
    public String createComment(@PathVariable Long postId, @ModelAttribute CommentRequest request) {
        commentService.createComment(postId, request);
        return "redirect:/posts/" + postId;
    }

    // 댓글 삭제
    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId, @RequestParam Long postId) {
        commentService.deleteComment(commentId);
        return "redirect:/posts/" + postId;
    }
}
