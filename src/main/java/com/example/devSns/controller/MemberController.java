package com.example.devSns.controller;

import com.example.devSns.dto.*;
import com.example.devSns.entity.Member;
import com.example.devSns.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService MemberService;

    // 회원 조회
    @GetMapping("/{id}")
    public MemberResponse getMember(@PathVariable Long id) {
        Member member = MemberService.findMemberById(id);
        return new MemberResponse(member);
    }

    // 회원 검색
    @GetMapping("/search")
    public List<MemberResponse> search(@RequestParam String keyword) {
        return MemberService.searchMembers(keyword).stream().map(MemberResponse::new).toList();
    }

    // 회원 작성 글 조회
    @GetMapping("/{id}/posts")
    public List<PostResponse> getMemberPosts(@PathVariable Long id) {
        return MemberService.getPostsByMember(id);
    }

    // 회원 작성 댓글 조회
    @GetMapping("/{id}/comments")
    public List<CommentResponse> getMemberComments(@PathVariable Long id) {
        return MemberService.getCommentsByMember(id);
    }

    // 회원이 좋아요한 글 조회
    @GetMapping("/{id}/likes")
    public List<PostResponse> getMemberLikes(@PathVariable Long id) {
        return MemberService.getLikedPosts(id);
    }
}
