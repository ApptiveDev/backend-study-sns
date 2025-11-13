package com.example.devSns.like;

import com.example.devSns.comment.Comment;
import com.example.devSns.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByMemberAndComment(Member member, Comment comment);
    long countByComment(Comment comment);
}
