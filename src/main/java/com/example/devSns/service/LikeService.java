package com.example.devSns.service;

import com.example.devSns.domain.Member;
import com.example.devSns.domain.Post;
import com.example.devSns.domain.PostLike;
import com.example.devSns.dto.LikeRequestDto;
import com.example.devSns.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostLikeRepository postLikeRepository;
    private final MemberService memberService; // Member 조회용
    private final PostService postService; // Post 조회용

    // 좋아요 토글 (눌렀으면 취소, 안 눌렀으면 추가)
    @Transactional
    public String toggleLike(LikeRequestDto requestDto) {
        Member member = memberService.getMemberById(requestDto.getMemberId());
        Post post = postService.getPostById(requestDto.getPostId());

        Optional<PostLike> existingLike = postLikeRepository.findByMemberAndPost(member, post);

        if (existingLike.isPresent()) {
            // 이미 좋아요 누름 -> 좋아요 취소
            postLikeRepository.delete(existingLike.get());
            return "Like removed";
        } else {
            // 좋아요 안 누름 -> 좋아요 추가
            PostLike newLike = new PostLike(member, post);
            postLikeRepository.save(newLike);
            return "Like added";
        }
    }
}