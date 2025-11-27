package com.example.devSns.controllers;

import com.example.devSns.dto.JwtDTO;
import com.example.devSns.dto.LoginDTO;
import com.example.devSns.dto.UserDTO;
import com.example.devSns.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sns")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserDTO userInfo) {
        String resp = userService.signUp(userInfo);
        ResponseEntity<String> response = new ResponseEntity(resp, HttpStatus.OK);
        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDTO> login(@RequestBody LoginDTO loginDTO) {
        return new ResponseEntity<>(userService.login(loginDTO), HttpStatus.OK);
    }
}
