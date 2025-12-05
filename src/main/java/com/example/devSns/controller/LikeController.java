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
    private final LikeService LikeService;

    @PostMapping("/toggle")
    public ResponseEntity<LikeResponse> toggleLike(@RequestBody LikeToggleRequest request){
        LikeService.toggleLike(request.getMemberId(), request.getPostId());
        long count = LikeService.getLikeCount(request.getPostId());
        return ResponseEntity.ok(new LikeResponse(request.getPostId(),count,true));
    }

    @GetMapping("/count/{postId}")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long postId){
        long count = LikeService.getLikeCount(postId);
        return ResponseEntity.ok(count);
    }

}
