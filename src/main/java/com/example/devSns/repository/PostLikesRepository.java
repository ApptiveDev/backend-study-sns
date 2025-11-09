package com.example.devSns.repository;

import com.example.devSns.domain.Member;
import com.example.devSns.domain.Post;
import com.example.devSns.domain.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {

    boolean existsByMemberAndPost(Member member, Post post);
    Optional<PostLikes> findByMemberAndPost(Member member, Post post);
}
