package com.example.devSns.util;

import com.example.devSns.authorities.MemberDetailsService;
import com.example.devSns.authorities.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final MemberDetailsService memberDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
        throws IOException, ServletException {
        String authorizationHeader = req.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) { // Authorization header : 앞에 Bearer 있는지 확인
            String token = authorizationHeader.substring(7); // token 추출
            if (jwtUtil.validateToken(token)) {
                String userID = jwtUtil.getUserID(token);
                Role userRole = jwtUtil.getRole(token);

                UserDetails userDetail = memberDetailsService.loadUserByUsername(userID);
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            chain.doFilter(req, resp);
        }
    }
}
