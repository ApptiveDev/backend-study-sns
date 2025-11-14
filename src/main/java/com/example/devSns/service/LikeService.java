package com.example.devSns.service;

import com.example.devSns.entity.Like;
import com.example.devSns.entity.Member;
import com.example.devSns.entity.Post;
import com.example.devSns.repository.LikeRepository;
import com.example.devSns.repository.MemberRepository;
import com.example.devSns.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void toggleLike(Long memberId, Long postId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("member not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("post not found"));

        Optional<Like> existingLike = likeRepository.findByMemberAndPost(member, post);

        if(existingLike.isPresent()){
            likeRepository.delete(existingLike.get());
        } else {
            Like like = Like.create(member, post);
            likeRepository.save(like);
        }
    }

    public long getLikeCount(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("post not found"));
        return likeRepository.countByPost(post);
    }
}
