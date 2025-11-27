package com.example.devSns.util;

import com.example.devSns.authorities.Role;
import com.example.devSns.dto.UserDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${secretKey}")
    private String secretKey;

    public Jws<Claims> parseToken(String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public String generateToken(String loginID, String username, Role role) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        Map<String, Object> claims = new HashMap<>();

        claims.put("loginID", loginID);
        claims.put("username", username);
        claims.put("role", role);

        String jwtToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return jwtToken;
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch(JwtException e) {
            return false;
        }
    }

    public String getUserID(String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        Claims claims = parseToken(token).getBody();
        return claims.get("loginID", String.class);
    }

    public Role getRole(String token) {
        Claims claims = parseToken(token).getBody();
        return Role.valueOf(claims.get("role", String.class));
    }
}
