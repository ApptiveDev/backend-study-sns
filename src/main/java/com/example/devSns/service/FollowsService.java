package com.example.devSns.service;

import com.example.devSns.domain.Follows;
import com.example.devSns.domain.Member;
import com.example.devSns.dto.GenericDataDto;
import com.example.devSns.dto.follow.FollowCreateDto;
import com.example.devSns.exception.NotFoundException;
import com.example.devSns.repository.FollowsRepository;
import com.example.devSns.repository.MemberRepository;

public class FollowsService {
    private final FollowsRepository followsRepository;
    private final MemberRepository memberRepository;

    public FollowsService(FollowsRepository followsRepository, MemberRepository memberRepository) {
        this.followsRepository = followsRepository;
        this.memberRepository = memberRepository;
    }

    public Long follow(FollowCreateDto followCreateDto) {
        Member follower = memberRepository.findById(followCreateDto.followerId()).orElseThrow(()->new NotFoundException("Follower not found"));
        Member following = memberRepository.findById(followCreateDto.followingId()).orElseThrow(()->new NotFoundException("Following not found"));

        Follows follows = new Follows(follower, following);
        return followsRepository.save(follows).getId();
    }

    public void unfollow(GenericDataDto<Long> unfollowDto) {
        Follows follows = followsRepository.findById(unfollowDto.data())
                .orElseThrow(()->new NotFoundException("Follows not found"));

        followsRepository.delete(follows);
    }



}
