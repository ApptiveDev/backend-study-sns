package com.example.devSns.repositories;

import com.example.devSns.entities.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Posts, Long> {
    public List<Posts> findAll();
    public Posts findById(Integer id);
    public Posts findByUsername(String username);
}
