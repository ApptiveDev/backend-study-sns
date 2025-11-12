package com.example.devSns.service;

import com.example.devSns.domain.*;
import com.example.devSns.dto.commentLikes.CommentLikesRequestDto;
import com.example.devSns.dto.postLikes.PostLikesRequestDto;
import com.example.devSns.exception.InvalidRequestException;
import com.example.devSns.exception.NotFoundException;
import com.example.devSns.repository.CommentLikesRepository;
import com.example.devSns.repository.CommentRepository;
import com.example.devSns.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    protected Long saveLikes(CommentLikes likes) {
        return commentLikesRepository.save(likes).getId();
    }

    @Override
    protected CommentLikes findLikes(Long targetId, Long memberId) {
        MemberAndComment mc = findMemberAndComment(targetId, memberId);

        return commentLikesRepository.findByMemberAndComment(mc.member(), mc.comment())
                .orElseThrow(()->new NotFoundException("Comment not liked"));
    }

    @Override
    protected void deleteLikes(CommentLikes likes) {
        commentLikesRepository.delete(likes);
    }

    private record MemberAndComment(Member member, Comment comment) {}
    private MemberAndComment findMemberAndComment(Long targetId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(()->new NotFoundException("Member not found"));
        Comment comment = commentRepository.findById(targetId).orElseThrow(()->new NotFoundException("Comment not found"));

        return new MemberAndComment(member, comment);
    }

}
