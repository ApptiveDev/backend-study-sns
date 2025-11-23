package com.example.devSns.service;

import com.example.devSns.domain.*;
import com.example.devSns.dto.member.MemberResponseDto;
import com.example.devSns.exception.NotFoundException;
import com.example.devSns.repository.CommentLikesRepository;
import com.example.devSns.repository.CommentRepository;
import com.example.devSns.repository.MemberRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CommentLikesService extends LikesService<CommentLikes> {
    private final CommentLikesRepository commentLikesRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    public CommentLikesService(CommentLikesRepository commentLikesRepository, MemberRepository memberRepository, CommentRepository commentRepository) {
        this.commentLikesRepository = commentLikesRepository;
        this.memberRepository = memberRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    protected CommentLikes createLikes(Long targetId, Long memberId) {
        MemberAndComment mc = findMemberAndComment(targetId, memberId);

        return new CommentLikes(mc.member(), mc.comment());
    }

    @Override
    protected void saveLikes(CommentLikes likes) {
        commentLikesRepository.save(likes);
    }

    @Override
    protected Optional<CommentLikes> findLikes(Long targetId, Long memberId) {
        MemberAndComment mc = findMemberAndComment(targetId, memberId);
        return commentLikesRepository.findByMemberAndComment(mc.member(), mc.comment());
    }

    @Override
    protected void deleteLikes(CommentLikes likes) {
        commentLikesRepository.delete(likes);
    }


    @Override
    public Slice<MemberResponseDto> findWhoLiked(Pageable pageable, Long targetId) {
        Comment comment = commentRepository.findById(targetId).orElseThrow(()->new NotFoundException("Comment not found"));
        return commentLikesRepository.findLikedUsersByComment(comment, pageable);
    }


    private record MemberAndComment(Member member, Comment comment) {}
    private MemberAndComment findMemberAndComment(Long targetId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(()->new NotFoundException("Member not found"));
        Comment comment = commentRepository.findById(targetId).orElseThrow(()->new NotFoundException("Comment not found"));

        return new MemberAndComment(member, comment);
    }

}
