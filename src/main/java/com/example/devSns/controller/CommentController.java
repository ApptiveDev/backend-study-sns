package com.example.devSns.controller;

import com.example.devSns.dto.GenericDataDto;
import com.example.devSns.dto.PaginatedDto;
import com.example.devSns.dto.comment.CommentCreateDto;
import com.example.devSns.dto.comment.CommentResponseDto;
import com.example.devSns.dto.post.PostResponseDto;
import com.example.devSns.exception.InvalidRequestException;
import com.example.devSns.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @PostMapping
    public ResponseEntity<GenericDataDto<Long>> create(@RequestBody @Valid CommentCreateDto commentCreateDto) {
        Long id = commentService.join(commentCreateDto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uri).body(new GenericDataDto<>(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> getOne(@PathVariable @Positive Long id) {
        CommentResponseDto comment = commentService.findOne(id);
        return ResponseEntity.ok().body(comment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/postGroup/{postId}")
    public ResponseEntity<PaginatedDto<List<CommentResponseDto>>> getAsPaginated(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime before,
            @PathVariable @Positive Long postId
    ) {
        PaginatedDto<List<CommentResponseDto>> posts = commentService.findAsPaginated(new GenericDataDto<>(before), postId);
        return ResponseEntity.ok().body(posts);
    }

    @PatchMapping("/{id}/likes")
    public ResponseEntity<CommentResponseDto> like(@PathVariable @Positive Long id) {
        CommentResponseDto comment = commentService.like(id);
        return ResponseEntity.ok().body(comment);
    }

    @PatchMapping("/{id}/contents")
    public ResponseEntity<CommentResponseDto> contents(@PathVariable @Positive Long id,
                                                       @RequestBody @Valid GenericDataDto<String> contentsDto) {
        if (contentsDto == null) throw new InvalidRequestException("Invalid request.");
        CommentResponseDto comment = commentService.updateContent(id, contentsDto);
        return ResponseEntity.ok().body(comment);
    }
}
