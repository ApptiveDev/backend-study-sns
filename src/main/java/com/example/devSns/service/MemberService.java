package com.example.devSns.service;

import com.example.devSns.domain.Comment;
import com.example.devSns.domain.Member;
import com.example.devSns.domain.Post;
import com.example.devSns.domain.PostLike;
import com.example.devSns.dto.CommentResponseDto;
import com.example.devSns.dto.MemberProfileResponseDto;
import com.example.devSns.dto.MemberResponseDto;
import com.example.devSns.dto.MemberSignUpRequestDto;
import com.example.devSns.dto.PostResponseDto;
import com.example.devSns.repository.CommentRepository;
import com.example.devSns.repository.MemberRepository;
import com.example.devSns.repository.PostLikeRepository;
import com.example.devSns.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

    // 회원가입
    @Transactional
    public MemberResponseDto signup(MemberSignUpRequestDto requestDto) {
        // 닉네임 중복 확인 (간단하게)
        if (memberRepository.findByNickname(requestDto.getNickname()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
        // TODO: 패스워드 암호화
        Member member = new Member(requestDto.getUsername(), requestDto.getPassword(), requestDto.getNickname());
        Member savedMember = memberRepository.save(member);
        return new MemberResponseDto(savedMember);
    }

    // 회원 ID로 조회 (내부 로직용)
    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }

    // 회원 닉네임으로 검색
    @Transactional(readOnly = true)
    public MemberResponseDto searchByNickname(String nickname) {
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        return new MemberResponseDto(member);
    }

    // 회원 프로필 조회 (작성한 글, 댓글, 좋아요한 글)
    @Transactional(readOnly = true)
    public MemberProfileResponseDto getMemberProfile(Long id) {
        Member member = getMemberById(id);

        // 1. 회원이 작성한 글
        List<PostResponseDto> posts = postRepository.findByMemberId(id).stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        // 2. 회원이 작성한 댓글 (대댓글 구조 미포함, 단순 리스트)
        List<CommentResponseDto> comments = commentRepository.findByMemberId(id).stream()
                .map(CommentResponseDto::from)
                .collect(Collectors.toList());

        // 3. 회원이 좋아요한 글
        List<PostLike> likes = postLikeRepository.findByMember(member);
        List<PostResponseDto> likedPosts = likes.stream()
                .map(PostLike::getPost)
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return new MemberProfileResponseDto(member, posts, comments, likedPosts);
    }
}