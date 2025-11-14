package com.example.devSns.service;

import com.example.devSns.entity.CommentEntity;
import com.example.devSns.entity.LikeEntity;
import com.example.devSns.entity.MemberEntity;
import com.example.devSns.entity.PostEntity;
import com.example.devSns.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 멤버 생성
    public MemberEntity createMember(String username, String email) {
        MemberEntity member = MemberEntity.create(username, email);
        return memberRepository.save(member);
    }

    // 단건 조회
    public MemberEntity getMember(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }

    // 검색
    public List<MemberEntity> search(String keyword) {
        return memberRepository.findByUsernameContaining(keyword);
    }

    // 멤버의 게시글 조회
    public List<PostEntity> getMemberPosts(Long id) {
        return getMember(id).getPosts();
    }

    // 멤버의 댓글 조회
    public List<CommentEntity> getMemberComments(Long id) {
        return getMember(id).getComments();
    }

    // 멤버가 누른 좋아요 목록
    public List<LikeEntity> getMemberLikes(Long id) {
        return getMember(id).getLikes();
    }

    // 멤버 조회
    public List<MemberEntity> getAllMembers() {
        return memberRepository.findAll();
    }

}
