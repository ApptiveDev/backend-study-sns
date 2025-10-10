package com.example.devSns.task.service;

import com.example.devSns.task.dto.PostCreateRequestDto;
import com.example.devSns.task.dto.PostResponseDto;
import com.example.devSns.task.dto.PostUpdateRequestDto;
import com.example.devSns.task.entity.Post;
import com.example.devSns.task.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용으로 설정
public class PostService {
    private final PostRepository postRepository;

    // 1. 생성
    @Transactional
    public PostResponseDto createPost(PostCreateRequestDto requestDto){
        Post post  = requestDto.toEntity();
        Post savedPost = postRepository.save(post);
        return new PostResponseDto(savedPost);
    }

    // 2. 전체 조회
    public List<PostResponseDto> findAllPosts(){
        return postRepository.findAll().stream()
                .map(PostResponseDto::new) // Post 엔티티를 PostResponseDto로 변환
                .collect(Collectors.toList());
    }

    // 3. 아이디로 조회
    public PostResponseDto findPostById(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + id));
        return new PostResponseDto(post);
    }

    // 4. 게시물 수정
    @Transactional
    public PostResponseDto updatePost(Long id, PostUpdateRequestDto requestDto){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + id));

        post.update(requestDto.postContent());

        return new PostResponseDto(post);
    }

    // 5. 삭제
    @Transactional
    public void deletePost(Long id){
        if(!postRepository.existsById(id)){
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + id);
        }
        postRepository.deleteById(id);
    }
}
