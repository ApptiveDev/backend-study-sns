package com.example.devSns.repositories;

import com.example.devSns.entities.Posts;
import com.example.devSns.entities.Replies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Replies, Long> {
    public List<Replies> findByPosts(Posts posts);
    public Replies findById(long id);
}
