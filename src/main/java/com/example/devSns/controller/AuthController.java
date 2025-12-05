package com.example.devSns.controller;

import com.example.devSns.dto.LoginRequest;
import com.example.devSns.dto.LoginResponse;
import com.example.devSns.dto.SignUpRequest;
import com.example.devSns.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private MemberService MemberService;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequest signUpRequest) {
        // 이메일 중복 체크
        if (MemberService.isEmailExists(signUpRequest.getEmail())) {
            return ResponseEntity.status(400).body("Email already exists");
        }

        // 회원가입 처리
        MemberService.register(signUpRequest);
        return ResponseEntity.status(201).body("Sign up successful");
    }

    // 로그인(JWT 발급)
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = MemberService.login(loginRequest);
        if (token == null) {
            return ResponseEntity.status(401).body(new LoginResponse("Login failed"));  // 로그인 실패 시
        }
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
