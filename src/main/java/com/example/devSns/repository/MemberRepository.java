package com.example.devSns.repository;

import com.example.devSns.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> FindByUsername(String username);
    List<Member> FindByUsernameContaining(String keyword);
    Optional<Member> FindByEmail(String email);
}
