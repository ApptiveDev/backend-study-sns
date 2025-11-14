package com.example.devSns.controller;

import com.example.devSns.dto.LikeRequestDto;
import com.example.devSns.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    // 좋아요 토글 API
    @PostMapping("/toggle")
    public ResponseEntity<String> toggleLike(@RequestBody LikeRequestDto requestDto) {
        String result = likeService.toggleLike(requestDto);
        return ResponseEntity.ok(result);
    }
}