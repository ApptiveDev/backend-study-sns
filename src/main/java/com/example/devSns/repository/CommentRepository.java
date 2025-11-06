package com.example.devSns.repository;

import com.example.devSns.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Comment에서 연관된 Post 객체의 id 기준
    List<Comment> findByPost_Id(Long postId);
}
