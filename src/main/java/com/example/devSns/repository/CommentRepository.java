package com.example.devSns.repository;

import com.example.devSns.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시글의 모든 최상위 댓글을 조회 (대댓글이 아닌 댓글)
    List<Comment> findByPostIdAndParentIsNull(Long postId);
}