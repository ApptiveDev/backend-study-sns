package com.example.devSns.web;

import com.example.devSns.service.CommentService;
import com.example.devSns.web.dto.CommentCreateRequest;
import com.example.devSns.web.dto.CommentUpdateRequest;
import com.example.devSns.web.dto.CommentResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    private final CommentService svc;

    public CommentController(CommentService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<CommentResponse> list(@PathVariable Long postId) {
        return svc.list(postId).stream().map(CommentResponse::from).toList();
    }

    @GetMapping("/{commentId}")
    public CommentResponse get(@PathVariable Long postId, @PathVariable Long commentId) {
        return CommentResponse.from(svc.get(postId, commentId));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommentResponse create(@PathVariable Long postId,
                                  @Valid @RequestBody CommentCreateRequest req) {
        return CommentResponse.from(svc.create(postId, req.content()));
    }

    @PutMapping("/{commentId}")
    public CommentResponse update(@PathVariable Long postId, @PathVariable Long commentId,
                                  @Valid @RequestBody CommentUpdateRequest req) {
        return CommentResponse.from(svc.update(postId, commentId, req.content()));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long postId, @PathVariable Long commentId) {
        svc.delete(postId, commentId);
    }
}
