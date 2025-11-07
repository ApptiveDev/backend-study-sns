package com.example.devSns.task.controller;

import com.example.devSns.task.dto.*;
import com.example.devSns.task.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts") // 기본 url 경로
@RequiredArgsConstructor
public class Controller {
    private final PostService postService;

    // 1. 생성 : Post /api/posts
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostCreateRequestDto requestDto){
        PostResponseDto createdPost = postService.createPost(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    // 2. 전체 조회 : GET /api/posts
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts(){
        List<PostResponseDto> posts = postService.findAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 3. 아이디로 조회 : GET /api/posts/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id){
        PostResponseDto post = postService.findPostById(id);
        return ResponseEntity.ok(post);
    }

    // 4. 수정 : PUT /api/posts/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id,@RequestBody PostUpdateRequestDto requestDto){
        PostResponseDto updatedPost = postService.updatePost(id, requestDto);
        return ResponseEntity.ok(updatedPost);
    }

    // 5. 삭제 (Delete) : DELETE /api/posts/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
