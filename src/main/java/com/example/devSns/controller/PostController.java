package com.example.devSns.controller;

import com.example.devSns.entity.Post;
import com.example.devSns.service.PostService;
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
    public List<Post> getAllPosts(){
        return postService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id){
        Post post = postService.findById(id);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public Post createPost(@RequestBody Post post){
        return postService.save(post);
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody Post updatedPost){

        return postService.updatePost(id,updatedPost);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id){
        postService.delete(id);
    }
}