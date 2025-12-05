package com.example.devSns.controller;

import com.example.devSns.dto.CommentCreateRequest;
import com.example.devSns.dto.CommentResponse;
import com.example.devSns.dto.CommentUpdateRequest;
import com.example.devSns.entity.Comment;
import com.example.devSns.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    private final CommentService CommentService;

    public CommentController(CommentService commentService) {
        this.CommentService = commentService;
    }

    @GetMapping
    public List<CommentResponse> getComments(@PathVariable Long postId) {
        return CommentService.GetCommentByPost(postId).stream()
                .map(CommentResponse::new)
                .toList();
    }

    @PostMapping
    public CommentResponse createComment(@PathVariable Long postId, @RequestBody CommentCreateRequest request) {
        Comment created = CommentService.AddComment(postId, request);
        return new CommentResponse(created);
    }


}