package com.example.devSns.service;

import com.example.devSns.domain.Follows;
import com.example.devSns.domain.Member;
import com.example.devSns.dto.GenericDataDto;
import com.example.devSns.dto.follow.FollowRequestDto;
import com.example.devSns.exception.NotFoundException;
import com.example.devSns.repository.FollowsRepository;
import com.example.devSns.repository.MemberRepository;
import org.springframework.stereotype.Service;


@Service
public class FollowsService {
    private final FollowsRepository followsRepository;
    private final MemberRepository memberRepository;

    public FollowsService(FollowsRepository followsRepository, MemberRepository memberRepository) {
        this.followsRepository = followsRepository;
        this.memberRepository = memberRepository;
    }

    public Long follow(FollowRequestDto followRequestDto) {
        FollowsDto fd = findFollowerAndFollowing(followRequestDto);

        Follows follows = new Follows(fd.follower(), fd.following());
        return followsRepository.save(follows).getId();
    }

    public void unfollow(FollowRequestDto followRequestDto) {
        FollowsDto fd = findFollowerAndFollowing(followRequestDto);
        Follows follows = followsRepository.findByFollowerAndFollowing(fd.follower(), fd.following());
        followsRepository.delete(follows);
    }

    private record FollowsDto(Member follower, Member following) {}
    private FollowsDto findFollowerAndFollowing(FollowRequestDto followRequestDto) {
        Member follower = memberRepository.findById(followRequestDto.followerId()).orElseThrow(()->new NotFoundException("Follower not found"));
        Member following = memberRepository.findById(followRequestDto.followingId()).orElseThrow(()->new NotFoundException("Following not found"));

        return new FollowsDto(follower, following);
    }


}
