package com.example.devSns.service;

import com.example.devSns.entity.LikeEntity;
import com.example.devSns.entity.MemberEntity;
import com.example.devSns.entity.PostEntity;
import com.example.devSns.repository.LikeRepository;
import com.example.devSns.repository.MemberRepository;
import com.example.devSns.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LikeService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    public LikeService(MemberRepository memberRepository, PostRepository postRepository, LikeRepository likeRepository) {
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
    }

    // 좋아요 클릭
    public void like(Long postId, Long memberId) {
        MemberEntity member = memberRepository.findById(memberId).orElseThrow();
        PostEntity postEntity = postRepository.findById(postId).orElseThrow();

        // 중복 체크
        likeRepository.findByMemberAndPost(member, postEntity).ifPresent(like -> {
            throw new IllegalStateException("이미 좋아요를 눌렀습니다.");
        });

        likeRepository.save(LikeEntity.create(member, postEntity));
    }

    // 좋아요 취소
    public void cancel(Long postId, Long memberId) {
        MemberEntity member = memberRepository.findById(memberId).orElseThrow();
        PostEntity postEntity = postRepository.findById(postId).orElseThrow();

        LikeEntity like = likeRepository.findByMemberAndPost(member, postEntity)
                .orElseThrow(() -> new IllegalStateException("좋아요를 누른 적이 없습니다."));

        likeRepository.delete(like);
    }
}
