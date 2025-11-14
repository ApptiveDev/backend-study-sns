package com.example.devSns.repositories;

import com.example.devSns.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Long> {
    public Users findById(long userId);
    public List<Users> findByUsernameContaining(String partialUsername);
}
