package com.example.devSns.service;

import com.example.devSns.domain.Member;
import com.example.devSns.domain.Post;
import com.example.devSns.dto.PostRequestDto;
import com.example.devSns.dto.PostResponseDto;
import com.example.devSns.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberService memberService; // Member 조회용

    // 게시글 생성
    @Transactional
    public Post createPost(PostRequestDto requestDto) {
        Member member = memberService.getMemberById(requestDto.getMemberId());
        Post post = new Post(requestDto.getTitle(), requestDto.getContent(), member);
    }

    // 모든 게시글 조회
    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    // 특정 게시글 조회 (ID)
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID not found: " + id));
    }

    // 게시글 수정
    @Transactional
    public Post updatePost(PostRequestDto requestDto) {
        Post post = getPostById(requestDto.getId());

        // 간단한 권한 확인 (작성자 본인만 수정 가능)
        if (!post.getMember().getId().equals(requestDto.getMemberId())) {
            throw new IllegalArgumentException("You are not authorized to update this post.");
        }

        post.update(requestDto.getTitle(), requestDto.getContent());
        return postRepository.save(post);
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long id, Long memberId) {
        Post post = getPostById(id);

        // 간단한 권한 확인 (작성자 본인만 삭제 가능)
        if (!post.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("You are not authorized to delete this post.");
        }
        postRepository.deleteById(id);
    }
}