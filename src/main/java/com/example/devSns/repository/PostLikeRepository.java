package com.example.devSns.repository;

import com.example.devSns.domain.Member;
import com.example.devSns.domain.Post;
import com.example.devSns.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    // 특정 회원이 특정 게시글에 '좋아요'를 눌렀는지 확인
    Optional<PostLike> findByMemberAndPost(Member member, Post post);

    // 특정 회원이 '좋아요' 누른 모든 PostLike 조회 (프로필용)
    List<PostLike> findByMember(Member member);
}