package com.example.devSns.app.data.jpa.repository;

import com.example.devSns.app.data.jpa.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<PostEntity, Long> {
}
