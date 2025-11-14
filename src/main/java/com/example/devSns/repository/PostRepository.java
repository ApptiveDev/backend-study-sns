package com.example.devSns.repository;

import com.example.devSns.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // import 추가

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // 특정 회원이 작성한 모든 게시글 조회 (프로필용)
    List<Post> findByMemberId(Long memberId);
}