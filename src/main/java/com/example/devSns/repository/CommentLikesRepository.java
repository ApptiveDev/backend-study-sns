package com.example.devSns.repository;


import com.example.devSns.domain.Comment;
import com.example.devSns.domain.CommentLikes;
import com.example.devSns.domain.Member;
import com.example.devSns.domain.Post;
import com.example.devSns.dto.member.MemberResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentLikesRepository extends JpaRepository<CommentLikes, Long> {
    boolean existsByMemberAndComment(Member member, Comment comment);

    Optional<CommentLikes> findByMemberAndComment(Member member, Comment comment);

    @Query("""
        SELECT new com.example.devSns.dto.member.MemberResponseDto(
          m.id,
          m.nickname
        ) 
        FROM CommentLikes c
        JOIN c.member m
        WHERE c.comment = :comment
    """)
    Slice<MemberResponseDto> findLikedUsersByComment(Comment comment, Pageable pageable);

}
