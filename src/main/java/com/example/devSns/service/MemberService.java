package com.example.devSns.service;

import com.example.devSns.dto.CommentResponse;
import com.example.devSns.dto.PostResponse;
import com.example.devSns.entity.Comment;
import com.example.devSns.entity.Like;
import com.example.devSns.entity.Member;
import com.example.devSns.entity.Post;
import com.example.devSns.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Member join(Member member) {
        validateDuplicateMember(member.getUsername());
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(String username) {
        boolean exits = memberRepository.findByUsername(username).isPresent();
        if (exits) {
            throw new IllegalStateException("Already exists member "+username);
        }
    }
    public Member findMemberById(long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("member not found"));
    }

    public List<Member> searchMembers(String keyword){
        return memberRepository.findByUsernameContaining(keyword);
    }

    public List<PostResponse> getPostsByMember(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        return member.getPosts().stream()
                .map(PostResponse::new)
                .toList();
    }

    public List<CommentResponse> getCommentsByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        return member.getComments().stream()
                .map(CommentResponse::new)
                .toList();
    }
    public List<PostResponse> getLikedPosts(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        return member.getLikes().stream()
                .map(like -> new PostResponse(like.getPost()))
                .toList();
    }
    @Transactional(readOnly = true)
    public List<Like> getLikesByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        return member.getLikes();
    }


}

