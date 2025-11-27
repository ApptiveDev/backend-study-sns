package com.example.devSns.Login;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return loginService.login(request.getEmail(), request.getPassword());
    }

    @Data
    static class LoginRequest {
        private String email;
        private String password;
    }
}
