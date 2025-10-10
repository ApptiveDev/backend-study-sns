package com.example.devSns.controller;

import com.example.devSns.domain.Post;
import com.example.devSns.dto.PostRequestDto;
import com.example.devSns.dto.PostResponseDto;
import com.example.devSns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts") // http://.../api/posts 로 시작하는 모든 요청을 이 컨트롤러가 처리
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 1. 게시글 생성 (POST)
    @PostMapping
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto) {
        Post post = postService.createPost(requestDto);
        return new PostResponseDto(post);
    }

    // 2. 모든 게시글 조회 (GET)
    @GetMapping
    public List<PostResponseDto> getAllPosts() {
        return postService.getAllPosts().stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    // 3. 특정 게시글 조회 (GET)
    @GetMapping("/{id}")
    public PostResponseDto getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        return new PostResponseDto(post);
    }

    // 4. 게시글 수정 (PUT)
    @PutMapping
    public PostResponseDto updatePost(@RequestBody PostRequestDto requestDto) {
        Post updatedPost = postService.updatePost(requestDto);
        return new PostResponseDto(updatedPost);
    }

    // 5. 게시글 삭제 (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build(); // 성공적으로 처리되었음을 응답 (body는 없음)
    }
}