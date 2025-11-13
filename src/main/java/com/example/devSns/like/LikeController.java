package com.example.devSns.like;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{memberId}/{commentId}")
    public void toggleLike(@PathVariable Long memberId, @PathVariable Long commentId) {
        likeService.toggleLike(memberId, commentId);
    }

    @GetMapping("/{commentId}/count")
    public long countLikes(@PathVariable Long commentId) {
        return likeService.countLikes(commentId);
    }
}
