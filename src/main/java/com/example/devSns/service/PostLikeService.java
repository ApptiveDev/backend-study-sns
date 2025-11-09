package com.example.devSns.service;

import com.example.devSns.domain.Member;
import com.example.devSns.domain.Post;
import com.example.devSns.domain.PostLikes;
import com.example.devSns.dto.GenericDataDto;
import com.example.devSns.dto.postLikes.PostLikesRequestDto;
import com.example.devSns.exception.InvalidRequestException;
import com.example.devSns.exception.NotFoundException;
import com.example.devSns.repository.MemberRepository;
import com.example.devSns.repository.PostLikesRepository;
import com.example.devSns.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostLikeService {
    private final PostLikesRepository postLikesRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    public PostLikeService(PostLikesRepository postLikesRepository, MemberRepository memberRepository, PostRepository postRepository) {
        this.postLikesRepository = postLikesRepository;
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
    }

    public Long like(PostLikesRequestDto postLikesRequestDto) {
        Member member = memberRepository.findById(postLikesRequestDto.memberId()).orElseThrow(()->new NotFoundException("Member not found"));
        Post post = postRepository.findById(postLikesRequestDto.postId()).orElseThrow(()->new NotFoundException("Post not found"));

        if (postLikesRepository.existsByMemberAndPost(member, post)) {
            throw new InvalidRequestException("Post already liked");
        }

        PostLikes postLikes = new PostLikes(member, post);
        return postLikesRepository.save(postLikes).getId();
    }

    public void unlike(PostLikesRequestDto postLikesRequestDto) {
        Member member = memberRepository.findById(postLikesRequestDto.memberId()).orElseThrow(()->new NotFoundException("Member not found"));
        Post post = postRepository.findById(postLikesRequestDto.postId()).orElseThrow(()->new NotFoundException("Post not found"));

        PostLikes postLikes = postLikesRepository.findByMemberAndPost(member, post).orElseThrow(()->new InvalidRequestException("Post not liked"));
        postLikesRepository.delete(postLikes);
    }

}
