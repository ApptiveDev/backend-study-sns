package com.example.devSns.controller;

import com.example.devSns.domain.Post;
import com.example.devSns.dto.PostRequestDto;
import com.example.devSns.dto.PostResponseDto;
import com.example.devSns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    // 1. 게시글 생성

    @PostMapping
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto) {
        Post post = postService.createPost(requestDto);
        return new PostResponseDto(post);
    }

    // 2. 모든 게시글 조회
    @GetMapping
    public List<PostResponseDto> getAllPosts() {
        return postService.getAllPosts();
    }

    // 3. 특정 게시글 조회
    @GetMapping("/{id}")
    public PostResponseDto getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        return new PostResponseDto(post);
    }

    // 4. 게시글 수정
    @PutMapping
    public PostResponseDto updatePost(@RequestBody PostRequestDto requestDto) {
        // DTO에 memberId가 포함되어 서비스단에서 권한 확인
        Post updatedPost = postService.updatePost(requestDto);
        return new PostResponseDto(updatedPost);
    }

    // 5. 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @RequestParam Long memberId) {
        // 삭제는 memberId를 파라미터로 받아 권한 확인
        postService.deletePost(id, memberId);
        return ResponseEntity.ok().build();
    }
}