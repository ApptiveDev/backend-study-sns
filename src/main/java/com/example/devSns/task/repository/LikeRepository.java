package com.example.devSns.task.repository;

import com.example.devSns.task.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findAllByMemberId(Long memberId);

    // 이미 좋아요 눌렀는지 확인하기 위해 조회
    // Optional의 isPresent() 기능으로 좋아요 존재 여부 확인하기 편하게 함.
    Optional<Like> findByMemberIdAndPostId(Long memberId, Long postId);
}
