package com.example.devSns.repository;

import com.example.devSns.entity.LikeEntity;
import com.example.devSns.entity.MemberEntity;
import com.example.devSns.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    // 특정 멤버가 특정 게시글에 좋아요 눌렀는지 확인
    Optional<LikeEntity> findByMemberAndPost(MemberEntity member, PostEntity postEntity);

    // 특정 게시글 전체 좋아요 조회
    List<LikeEntity> findByPost(PostEntity post);

    // 특정 멤버가 누른 모든 좋아요 조회
    List<LikeEntity> findByMember(MemberEntity member);

    // 좋아요 개수 조회
    int countByPostId(Long postId);
}
