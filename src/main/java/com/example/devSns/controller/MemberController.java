package com.example.devSns.controller;

import com.example.devSns.entity.MemberEntity;
import com.example.devSns.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

     // 전체 멤버 목록
    @GetMapping
    public String list(Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        return "member-list";
    }

    // 멤버 상세 페이지
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        MemberEntity member = memberService.getMember(id);

        model.addAttribute("member", member);
        model.addAttribute("posts", member.getPosts());
        model.addAttribute("comments", member.getComments());

        return "member-detail";
    }

     // 멤버 생성
    @GetMapping("/new")
    public String newForm() {
        return "member-form";
    }

    // 멤버 생성 처리
    @PostMapping
    public String create(@RequestParam String username,
                         @RequestParam String email) {

        memberService.createMember(username, email);
        return "redirect:/members";
    }

     // 멤버 검색
     // /members/search?keyword=abc
    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) {
        model.addAttribute("members", memberService.search(keyword));
        model.addAttribute("keyword", keyword);
        return "member-search";
    }
}
