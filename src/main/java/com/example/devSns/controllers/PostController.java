package com.example.devSns.controllers;

import com.example.devSns.dto.PostDTO;
import com.example.devSns.dto.PostResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
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
    public ResponseEntity<List<PostResponse>> showPost(@PathVariable String username) {
        List<PostResponse> post = postService.findByUsername(username);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<PostResponse> addPost(@RequestBody PostDTO postDTO) {
        PostResponse postResponse = postService.save(postDTO);
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @RequestBody PostDTO postDTO) {
        PostResponse postResponse = postService.update(id, postDTO);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        postService.delete(id);
        return new ResponseEntity<>("Post deleted", HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        ResponseEntity<String> response =
                new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        return response;
    }

    @ExceptionHandler
    public ResponseEntity<String> handleDataAccessException(DataAccessException e) {
        ResponseEntity<String> response =
                new ResponseEntity<>("DB 오류", HttpStatus.INTERNAL_SERVER_ERROR);
        return response;
    }
}
