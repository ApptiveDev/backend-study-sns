package com.example.devSns.controller;

import com.example.devSns.dto.GenericDataDto;
import com.example.devSns.dto.PaginatedDto;
import com.example.devSns.dto.comment.CommentResponseDto;
import com.example.devSns.service.CommentService;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class PostCommentController {
    private final CommentService commentService;

    public PostCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<PaginatedDto<List<CommentResponseDto>>> getAsPaginated(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime before,
            @PathVariable @Positive Long postId
    ) {
        PaginatedDto<List<CommentResponseDto>> posts = commentService.findAsPaginated(new GenericDataDto<>(before), postId);
        return ResponseEntity.ok().body(posts);
    }

}
