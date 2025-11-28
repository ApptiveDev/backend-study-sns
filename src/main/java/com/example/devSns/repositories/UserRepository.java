package com.example.devSns.repositories;

import com.example.devSns.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    public Users findById(long userId);
    public List<Users> findByUsernameContaining(String partialUsername);
    public Optional<Users> findByLoginID(String loginID);
}
