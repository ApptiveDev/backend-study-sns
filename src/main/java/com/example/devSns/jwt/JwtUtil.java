package com.example.devSns.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // 적절한 비밀키 생성 (HS256에 적합한 256비트 비밀키)
    private Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);  // HS256에 적합한 비밀키 생성

    // 토큰 생성
    public String generateToken(Long memberId, String email) {
        return Jwts.builder()
                .setSubject(String.valueOf(memberId))  // 사용자 ID
                .claim("email", email)  // 이메일을 추가
                .setIssuedAt(new Date())  // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))  // 만료 시간 (1시간)
                .signWith(secretKey)  // 서명 (secretKey 사용)
                .compact();
    }

    // 토큰 파싱
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)  // 동일한 비밀키를 사용하여 파싱
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
