package com.example.devSns.repository;

import com.example.devSns.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // <엔티티 이름, ID의 타입>
}