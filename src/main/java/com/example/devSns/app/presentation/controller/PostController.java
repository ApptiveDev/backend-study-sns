package com.example.devSns.app.presentation.controller;

import com.example.devSns.app.domain.model.Post;
import com.example.devSns.app.domain.utils.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 전체 조회
    @GetMapping
    public ResponseEntity<List<Post>> getAll() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<Post> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    // 생성
    @PostMapping
    public ResponseEntity<Post> create(@RequestParam String content,
                                       @RequestParam String authorName) {
        return ResponseEntity.ok(postService.createPost(content, authorName));
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable Long id,
                                       @RequestParam String content) {
        return ResponseEntity.ok(postService.updatePost(id, content));
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    // 좋아요 증가
    @PostMapping("/{id}/like")
    public ResponseEntity<Post> like(@PathVariable Long id) {
        return ResponseEntity.ok(postService.likePost(id));
    }
}
