package com.example.devSns.controller;

import com.example.devSns.dto.MemberLoginRequestDto;
import com.example.devSns.dto.MemberResponseDto;
import com.example.devSns.dto.MemberSignUpRequestDto;
import com.example.devSns.dto.TokenRequestDto;
import com.example.devSns.dto.TokenDto;
import com.example.devSns.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberSignUpRequestDto requestDto) {
        return ResponseEntity.ok(authService.signup(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberLoginRequestDto requestDto) {
        return ResponseEntity.ok(authService.login(requestDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto requestDto) {
        return ResponseEntity.ok(authService.reissue(requestDto));
    }
}