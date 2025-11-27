package com.example.devSns.global;

import com.example.devSns.Jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public AuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String auth = request.getHeader("Authorization");

        if (auth == null || !auth.startsWith("Bearer ")) {
            response.sendError(401, "Missing token");
            return false;
        }

        String token = auth.substring(7);

        if (!jwtUtil.validateToken(token)) {
            response.sendError(401, "Invalid token");
            return false;
        }

        Long memberId = jwtUtil.extractMemberId(token);
        request.setAttribute("memberId", memberId);

        return true;
    }
}

