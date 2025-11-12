package com.example.devSns.repository;


import com.example.devSns.domain.Comment;
import com.example.devSns.domain.CommentLikes;
import com.example.devSns.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikesRepository extends JpaRepository<CommentLikes, Long> {
    boolean existsByMemberAndComment(Member member, Comment comment);

    Optional<CommentLikes> findByMemberAndComment(Member member, Comment comment);
}
