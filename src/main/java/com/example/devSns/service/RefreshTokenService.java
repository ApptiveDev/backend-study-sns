package com.example.devSns.service;

import com.example.devSns.domain.Member;
import com.example.devSns.domain.RefreshToken;
import com.example.devSns.domain.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshValidityInSeconds;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /** 새 리프레시 토큰 생성 + 저장 */
    public RefreshToken create(Member member) {
        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plusSeconds(refreshValidityInSeconds);

        RefreshToken refreshToken = RefreshToken.builder()
                .member(member)
                .token(token)
                .expiryDate(expiryDate)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    /** 토큰 문자열로 검증 (존재 + 만료 체크) */
    public RefreshToken validate(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "유효하지 않은 리프레시 토큰입니다."
                ));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "리프레시 토큰이 만료되었습니다. 다시 로그인 해주세요."
            );
        }

        return refreshToken;
    }

    /** 해당 회원의 모든 리프레시 토큰 제거 (로그아웃 등에서 사용 가능) */
    public void deleteByMember(Member member) {
        refreshTokenRepository.deleteByMember(member);
    }
}
