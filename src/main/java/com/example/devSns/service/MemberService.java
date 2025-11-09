package com.example.devSns.service;

import com.example.devSns.domain.Follows;
import com.example.devSns.domain.Member;
import com.example.devSns.dto.follow.FollowCreateDto;
import com.example.devSns.dto.member.MemberCreateDto;
import com.example.devSns.dto.member.MemberResponseDto;
import com.example.devSns.exception.NotFoundException;
import com.example.devSns.repository.FollowsRepository;
import com.example.devSns.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final FollowsRepository followsRepository;

    public MemberService(MemberRepository memberRepository, FollowsRepository followsRepository) {
        this.memberRepository = memberRepository;
        this.followsRepository = followsRepository;
    }

    @Transactional
    public Long create(MemberCreateDto memberCreateDto) {
        Member member = new Member(memberCreateDto.nickname());
        return memberRepository.save(member).getId();
    }

    public MemberResponseDto getOne(Long id) {
        Member member =  memberRepository.findById(id).orElseThrow(()->new NotFoundException("Member not found"));

        return new MemberResponseDto(member.getId(), member.getNickname());
    }



}
