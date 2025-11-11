package com.example.devSns.like;

import com.example.devSns.comment.Comment;
import com.example.devSns.comment.CommentRepository;
import com.example.devSns.member.Member;
import com.example.devSns.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LikeService {

    private final LikeRepository likeRepo;
    private final MemberRepository memberRepo;
    private final CommentRepository commentRepo;

    public LikeService(LikeRepository likeRepo, MemberRepository memberRepo, CommentRepository commentRepo) {
        this.likeRepo = likeRepo;
        this.memberRepo = memberRepo;
        this.commentRepo = commentRepo;
    }

    public void toggleLike(Long memberId, Long commentId) {
        Member member = memberRepo.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("No member found"));
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("No comment found"));

        likeRepo.findByMemberAndComment(member, comment)
                .ifPresentOrElse(
                        likeRepo::delete,
                        () -> likeRepo.save(new Like(member, comment))
                );
    }

    public long countLikes(Long commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("No comment found"));
        return likeRepo.countByComment(comment);
    }
}
