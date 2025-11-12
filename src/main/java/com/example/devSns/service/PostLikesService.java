package com.example.devSns.service;

import com.example.devSns.domain.Member;
import com.example.devSns.domain.Post;
import com.example.devSns.domain.PostLikes;
import com.example.devSns.dto.postLikes.PostLikesRequestDto;
import com.example.devSns.exception.InvalidRequestException;
import com.example.devSns.exception.NotFoundException;
import com.example.devSns.repository.MemberRepository;
import com.example.devSns.repository.PostLikesRepository;
import com.example.devSns.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostLikesService extends LikesService<PostLikes> {
    private final PostLikesRepository postLikesRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    public PostLikesService(PostLikesRepository postLikesRepository, MemberRepository memberRepository, PostRepository postRepository) {
        this.postLikesRepository = postLikesRepository;
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
    }

    @Override
    protected PostLikes createLikes(Long targetId, Long memberId) {
        MemberAndPost mp = findMemberAndPost(targetId, memberId);

        return new PostLikes(mp.member(), mp.post());
    }

    @Override
    protected PostLikes findLikes(Long targetId, Long memberId) {
        MemberAndPost mp = findMemberAndPost(targetId, memberId);

        return postLikesRepository.findByMemberAndPost(mp.member(), mp.post())
                .orElseThrow(()->new NotFoundException("Post not liked"));
    }

    @Override
    protected Long saveLikes(PostLikes postLikes) {
        return postLikesRepository.save(postLikes).getId();
    }

    @Override
    protected void deleteLikes(PostLikes postLikes) {
        postLikesRepository.delete(postLikes);
    }

    private record MemberAndPost(Member member, Post post) {}
    private MemberAndPost findMemberAndPost(Long targetId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(()->new NotFoundException("Member not found"));
        Post post = postRepository.findById(targetId).orElseThrow(()->new NotFoundException("Post not found"));

        return new MemberAndPost(member, post);
    }

}
