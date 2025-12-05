package com.example.devSns.controller;

import com.example.devSns.dto.PostCreateRequest;
import com.example.devSns.dto.PostResponse;
import com.example.devSns.dto.PostUpdateRequest;
import com.example.devSns.entity.Post;
import com.example.devSns.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService PostService;

    public PostController(PostService postService) {
        this.PostService = postService;
    }

    @GetMapping
    public Page<Post> getPosts(@RequestParam int page, @RequestParam int size){
        return PostService.FindAll(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id){
        Post post = PostService.FindById(id);
        return ResponseEntity.ok(new PostResponse(post));
    }

    @PostMapping
    public PostResponse createPost(@RequestBody PostCreateRequest request){
        Post created = PostService.CreatePost(request);
        return new PostResponse(created);
    }

    @PatchMapping("/{id}")
    public PostResponse updatePost(@PathVariable Long id, @RequestBody PostUpdateRequest request){
       Post updated = PostService.UpdatePost(id, request);
       return new PostResponse(updated);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id){
        PostService.delete(id);
    }
}