package com.example.devSns.controller;

import com.example.devSns.dto.CommentResponse;
import com.example.devSns.dto.MemberJoinRequest;
import com.example.devSns.dto.MemberResponse;
import com.example.devSns.dto.PostResponse;
import com.example.devSns.entity.Member;
import com.example.devSns.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{id}")
    public MemberResponse getMember(@PathVariable Long id) {
        Member member = memberService.findMemberById(id);
        return new MemberResponse(member);
    }

    @GetMapping("/search")
    public List<MemberResponse> search(@RequestParam String keyword) {
        return memberService.searchMembers(keyword).stream().map(MemberResponse::new).toList();
    }

    @GetMapping("/{id}/posts")
    public List<PostResponse> getMemberPosts(@PathVariable Long id) {
        return memberService.getPostsByMember(id);
    }

    @GetMapping("/{id}/comments")
    public List<CommentResponse> getMemberComments(@PathVariable Long id) {
        return memberService.getCommentsByMember(id);
    }

    @GetMapping("/{id}/likes")
    public List<PostResponse> getMemberLikes(@PathVariable Long id) {
        return memberService.getLikedPosts(id);
    }

    @PostMapping
    public MemberResponse createMember(@RequestBody MemberJoinRequest request){
        Member member = memberService.join(request.toEntity());
        return new MemberResponse(member);
    }

}
