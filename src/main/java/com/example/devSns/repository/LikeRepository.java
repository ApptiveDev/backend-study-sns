package com.example.devSns.repository;

import com.example.devSns.entity.Like;
import com.example.devSns.entity.Member;
import com.example.devSns.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByMemberAndPost(Member member, Post post);
    boolean existsByMemberAndPost(Member member, Post post);
    long countByPost(Post post);
}
