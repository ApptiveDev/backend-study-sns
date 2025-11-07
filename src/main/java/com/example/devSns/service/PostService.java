package com.example.devSns.service;

import com.example.devSns.domain.Post;
import com.example.devSns.dto.PostRequestDto;
import com.example.devSns.dto.PostResponseDto;
import com.example.devSns.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 만들어줍니다.
public class PostService {

    private final PostRepository postRepository;

    // 게시글 생성
    @Transactional
    public Post createPost(PostRequestDto requestDto) {
        Post post = new Post(requestDto.getTitle(), requestDto.getContent());
        return postRepository.save(post);
    }

    // 모든 게시글 조회
    public List<PostResponseDto>getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    // 특정 게시글 조회
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID not found: " + id));
    }

    // 게시글 수정
    @Transactional
    public Post updatePost(PostRequestDto requestDto) {
        Post post = getPostById(requestDto.getId());
//        post.setTitle(requestDto.getTitle());
//        post.setContent(requestDto.getContent()); 위험함! 무결성 해칠 수 있음
        post.update(requestDto.getTitle(), requestDto.getContent()); // 엔티티 메서드 사용
        return postRepository.save(post);
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}