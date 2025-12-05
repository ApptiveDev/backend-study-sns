package com.example.devSns;

import com.example.devSns.dto.LoginRequest;
import com.example.devSns.dto.SignUpRequest;
import com.example.devSns.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class LoginTest {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MemberService memberService;

    @Test
    void testPasswordEncoderMatches() {
        String rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        boolean isMatch = passwordEncoder.matches(rawPassword, encodedPassword);
        assertTrue(isMatch);
    }

    @Test
    void testSignUpAndLogin() {

        SignUpRequest signUpRequest = new SignUpRequest("user2", "user2@example.com", "password123");
        memberService.join(signUpRequest);  // 회원가입 호출

        LoginRequest loginRequest = new LoginRequest("user2@example.com", "password123");

        String token = memberService.login(loginRequest);

        assertNotNull(token);
    }
}
