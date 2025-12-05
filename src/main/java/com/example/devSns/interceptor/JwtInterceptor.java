package com.example.devSns.interceptor;

import com.example.devSns.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Missing or invalid token\"}");
            return false;
        }

        token = token.substring(7);

        try {
            // 토큰 파싱 및 검증
            Claims claims = jwtUtil.parseClaims(token);
            Long memberId = Long.valueOf(claims.getSubject());
            String email = claims.get("email", String.class);

            // 인증 정보를 request에 저장
            request.setAttribute("memberId", memberId);
            request.setAttribute("email", email);

            // 파싱된 토큰 정보 로그 출력
            System.out.println("Token parsed successfully: memberId=" + memberId + ", email=" + email);
            return true;
        } catch (Exception e) {
            // 토큰 파싱 실패 시
            System.out.println("Token parsing failed: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Invalid token\"}");
            return false;
        }
    }

}
