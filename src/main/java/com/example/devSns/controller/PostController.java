package com.example.devSns.controller;

import com.example.devSns.dto.PostCreateRequest;
import com.example.devSns.dto.PostResponse;
import com.example.devSns.dto.PostUpdateRequest;
import com.example.devSns.entity.Post;
import com.example.devSns.service.PostService;
import jakarta.persistence.PostUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<PostResponse> getAllPosts(){
        return postService.findAll().stream().map(PostResponse::new).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id){
        Post post = postService.findById(id);
        return ResponseEntity.ok(new PostResponse(post));
    }

    @PostMapping
    public PostResponse createPost(@RequestBody PostCreateRequest request){
        Post created = postService.createPost(request);
        return new PostResponse(created);
    }

    @PatchMapping("/{id}")
    public PostResponse updatePost(@PathVariable Long id, @RequestBody PostUpdateRequest request){
       Post updated = postService.updatePost(id, request);
       return new PostResponse(updated);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id){
        postService.delete(id);
    }
}