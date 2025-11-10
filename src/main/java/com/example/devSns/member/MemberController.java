package com.example.devSns.member;

import com.example.devSns.comment.Comment;
import com.example.devSns.task.Task;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public Member createMember(@RequestBody Member member) {
        return memberService.createMember(member);
    }

    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/{id}")
    public Member getMember(@PathVariable Long id) {
        return memberService.getMember(id);
    }

    @GetMapping("/search")
    public List<Member> searchMembers(@RequestParam String keyword) {
        return memberService.searchMembers(keyword); // ★ 그대로 사용 가능
    }
    // MemberController.java
    @GetMapping("/{id}/tasks")
    public List<Task> getMemberTasks(@PathVariable Long id) {
        Member member = memberService.getMember(id);
        return member.getTasks();
    }

    @GetMapping("/{id}/comments")
    public List<Comment> getMemberComments(@PathVariable Long id) {
        Member member = memberService.getMember(id);
        return member.getComments();
    }

}
