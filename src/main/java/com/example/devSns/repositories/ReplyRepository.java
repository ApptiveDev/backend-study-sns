package com.example.devSns.repositories;

import com.example.devSns.entities.Replies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Replies, Long> {
    public List<Replies> findByPostId(Long postId);
}
