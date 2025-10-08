package com.example.devSns.controller;

import com.example.devSns.entity.Post;
import com.example.devSns.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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
    public Post getPostById(@PathVariable Long id){
        return postService.findById(id).orElseThrow(()-> new RuntimeException("post not found"));
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