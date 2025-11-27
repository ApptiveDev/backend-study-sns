package com.example.devSns.repository;

import com.example.devSns.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 닉네임으로 회원 검색
    Optional<Member> findByNickname(String nickname);

    // 로그인 ID(username)로 회원 검색 (★ 이 부분이 빠져있어서 에러가 났습니다 ★)
    Optional<Member> findByUsername(String username);

    // 중복 가입 방지용 존재 여부 확인 (★ 이 부분도 같이 추가해주세요 ★)
    boolean existsByUsername(String username);
}