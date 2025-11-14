package com.example.devSns.repository;

import com.example.devSns.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostIdAndParentIsNull(Long postId);

    // 특정 회원이 작성한 모든 댓글 조회 (프로필용)
    List<Comment> findByMemberId(Long memberId);
}