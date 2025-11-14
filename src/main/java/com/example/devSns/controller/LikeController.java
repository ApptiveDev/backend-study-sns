package com.example.devSns.controller;

import com.example.devSns.dto.LikeResponse;
import com.example.devSns.dto.LikeToggleRequest;
import com.example.devSns.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/toggle")
    public ResponseEntity<LikeResponse> toggleLike(@RequestBody LikeToggleRequest request){
        likeService.toggleLike(request.getMemberId(), request.getPostId());
        long count = likeService.getLikeCount(request.getPostId());
        return ResponseEntity.ok(new LikeResponse(request.getPostId(),count,true));
    }

    @GetMapping("/count/{postId}")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long postId){
        long count = likeService.getLikeCount(postId);
        return ResponseEntity.ok(count);
    }

}
