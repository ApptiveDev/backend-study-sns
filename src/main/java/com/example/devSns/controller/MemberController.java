package com.example.devSns.controller;

import com.example.devSns.dto.MemberProfileResponseDto;
import com.example.devSns.dto.MemberResponseDto;
import com.example.devSns.dto.MemberSignUpRequestDto;
import com.example.devSns.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 1. 회원가입
    @PostMapping("/signup")
    public MemberResponseDto signup(@RequestBody MemberSignUpRequestDto requestDto) {
        return memberService.signup(requestDto);
    }

    // 2. 회원 닉네임으로 검색
    @GetMapping("/search")
    public MemberResponseDto searchByNickname(@RequestParam String nickname) {
        return memberService.searchByNickname(nickname);
    }

    // 3. 회원 프로필 조회 (작성한 글, 댓글, 좋아요한 글)
    @GetMapping("/{id}/profile")
    public MemberProfileResponseDto getMemberProfile(@PathVariable Long id) {
        return memberService.getMemberProfile(id);
    }
}