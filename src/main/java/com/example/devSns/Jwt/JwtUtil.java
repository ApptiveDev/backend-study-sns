package com.example.devSns.Jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {


    private final String secret;// 실제 프로젝트에서는 환경변수로 관리
    private final long EXPIRATION = 1000L * 60 * 60; // 1시간

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }
    public String createToken(Long memberId) {
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + EXPIRATION);

        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setIssuedAt(now)
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long extractMemberId(String token) {
        return Long.valueOf(
                Jwts.parser()
                        .setSigningKey(secret)
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()
        );
    }
}

