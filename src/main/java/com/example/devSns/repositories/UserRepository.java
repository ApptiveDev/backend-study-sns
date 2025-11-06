package com.example.devSns.repositories;

import com.example.devSns.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    public Users findById(long userId);
}
