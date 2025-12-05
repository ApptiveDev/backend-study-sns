package com.example.devSns.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/api/user")
    public String getUserInfo(@RequestAttribute("memberId") Long memberId, @RequestAttribute("email") String email) {
        return "Member ID: " + memberId + ", Email: " + email;
    }
}
