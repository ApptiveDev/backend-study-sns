package com.example.devSns.service;

import com.example.devSns.entity.Like;
import com.example.devSns.entity.Member;
import com.example.devSns.entity.Post;
import com.example.devSns.exception.EntityNotFoundException;
import com.example.devSns.repository.LikeRepository;
import com.example.devSns.repository.MemberRepository;
import com.example.devSns.repository.PostRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository LikeRepository;
    private final PostRepository PostRepository;
    private final MemberRepository MemberRepository;

    @Transactional
    public void toggleLike(Long memberId, Long postId){
        Member member = MemberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member",memberId));
        Post post = PostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post",postId));

        Optional<Like> existingLike = LikeRepository.FindByMemberAndPost(member, post);

        if(existingLike.isPresent()){
            LikeRepository.delete(existingLike.get());
        } else {
            Like like = Like.create(member, post);
            LikeRepository.save(like);
        }
    }

    public long getLikeCount(Long postId){
        Post post = PostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("post not found"));
        return LikeRepository.CountByPost(post);
    }
}
