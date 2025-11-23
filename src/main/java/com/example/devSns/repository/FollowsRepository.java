package com.example.devSns.repository;

import com.example.devSns.domain.Follows;
import com.example.devSns.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowsRepository extends JpaRepository<Follows, Long> {
    Follows findByFollowerAndFollowing(Member follower, Member following);
}
