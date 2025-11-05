package com.example.devSns.comment;

import com.example.devSns.comment.dto.CommentRequest;
import com.example.devSns.comment.dto.CommentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
public class CommentController {
    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> create(
            @PathVariable Long taskId,
            @RequestBody CommentRequest req) {
        return ResponseEntity.ok(service.create(taskId, req));
    }

    @GetMapping
    public List<CommentResponse> list(@PathVariable Long taskId) {
        return service.findByTask(taskId);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long taskId,
            @PathVariable Long commentId) {
        service.delete(commentId);
        return ResponseEntity.noContent().build();
    }
}
