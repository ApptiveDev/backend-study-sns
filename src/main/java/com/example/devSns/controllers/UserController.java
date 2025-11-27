package com.example.devSns.controllers;

import com.example.devSns.dto.LoginDTO;
import com.example.devSns.dto.UserDTO;
import com.example.devSns.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public String signup(@RequestBody UserDTO userInfo) {

    }

    @PostMapping
    public String login(@RequestBody LoginDTO loginDTO) {

    }
}
