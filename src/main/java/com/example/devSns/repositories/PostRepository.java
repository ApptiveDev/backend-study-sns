package com.example.devSns.repositories;

import com.example.devSns.entities.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Posts, Long> {
    public List<Posts> findAll();
    public Optional<Posts> findById(Integer id);
    public List<Posts> findByUsername(String username);
}
