package com.example.devSns.repository;

import com.example.devSns.domain.Member;
import com.example.devSns.domain.Post;
import com.example.devSns.domain.PostLikes;
import com.example.devSns.dto.member.MemberResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {

    boolean existsByMemberAndPost(Member member, Post post);
    Optional<PostLikes> findByMemberAndPost(Member member, Post post);

    @Query("""
        SELECT new com.example.devSns.dto.member.MemberResponseDto(
          m.id,
          m.nickname
        ) 
        FROM PostLikes p
        JOIN p.member m
        WHERE p.post = :post
    """)
    Slice<MemberResponseDto> findLikedUsersByPost(Post post, Pageable pageable);

}
