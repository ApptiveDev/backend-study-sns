package com.example.devSns.task.controller;

import com.example.devSns.task.dto.CommentCreateDto;
import com.example.devSns.task.dto.CommentResponseDto;
import com.example.devSns.task.dto.CommentUpdateDto;
import com.example.devSns.task.dto.PostResponseDto;
import com.example.devSns.task.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<PostResponseDto> createPost(@PathVariable Long postId, @RequestBody @Valid CommentCreateDto dto){
        commentService.createComment(postId,dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getAllComments(@PathVariable Long postId){
        List<CommentResponseDto> comments = commentService.getAllComments(postId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long commentId, @RequestBody @Valid CommentUpdateDto dto
    ){
        commentService.updateComment(commentId,dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId
    ){
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}

