package com.example.devSns.repository;

import com.example.devSns.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 닉네임으로 회원 검색 (검색 기능용)
    Optional<Member> findByNickname(String nickname);
}