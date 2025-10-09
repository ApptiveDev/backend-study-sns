package com.example.devSns.service;

import com.example.devSns.domain.Post;
import com.example.devSns.dto.PostRequestDto;
import com.example.devSns.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 특정 게시글 조회
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID not found: " + id));
    }

    // 게시글 수정
    @Transactional
    public Post updatePost(Long id, PostRequestDto requestDto) {
        Post post = getPostById(id);
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        return postRepository.save(post);
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}