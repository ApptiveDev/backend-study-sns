package com.example.devSns.controllers;

import com.example.devSns.dto.PostDTO;
import com.example.devSns.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.devSns.services.PostService;
import java.util.List;

@RestController
@RequestMapping("/sns")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/show")
    public ResponseEntity<List<PostResponse>> showPosts() {
        List<PostResponse> posts = postService.findAll();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/show/{username}")
    public ResponseEntity<PostResponse> showPost(@PathVariable String username) {
        PostResponse post = postService.findByUsername(username);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<PostResponse> addPost(@RequestBody PostDTO postDTO) {
        PostResponse postResponse = postService.save(postDTO);
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<PostResponse> updatePost(@RequestBody PostDTO postDTO) {
        PostResponse postResponse = postService.update(postDTO);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePost(@RequestBody PostDTO postDTO) {
        postService.delete(postDTO);
        return new ResponseEntity<>("Post deleted", HttpStatus.OK);
    }
}
