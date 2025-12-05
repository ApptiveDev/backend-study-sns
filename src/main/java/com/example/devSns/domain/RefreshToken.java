package com.example.devSns.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, unique = true, length = 200)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    @Builder
    private RefreshToken(Member member, String token, Instant expiryDate) {
        this.member = member;
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public void update(String token, Instant expiryDate) {
        this.token = token;
        this.expiryDate = expiryDate;
    }
}
