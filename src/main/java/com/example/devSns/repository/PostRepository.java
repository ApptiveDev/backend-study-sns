package com.example.devSns.repository;

import com.example.devSns.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Modifying
    @Query("update Post p set p.likeCount = p.likeCount + 1 where p.id = :id ")
    void incrementLikeById(Long id);

    List<Post> findTop15ByIdBeforeOrderByIdDesc(Long id);

    List<Post> findTop15ByCreatedAtBeforeOrderByCreatedAtDesc(LocalDateTime createdAt);
}
