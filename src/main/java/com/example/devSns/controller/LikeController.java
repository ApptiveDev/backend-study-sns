package com.example.devSns.controller;


import com.example.devSns.dto.GenericDataDto;
import com.example.devSns.dto.likes.LikesRequestDto;
import com.example.devSns.dto.post.PostResponseDto;
import com.example.devSns.service.LikesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public abstract class LikeController<T> {
    private final LikesService<T> likeService;

    public LikeController(LikesService<T> likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{id}/likes")
    public ResponseEntity<Void> like(@PathVariable @Positive Long id, @RequestBody @Valid GenericDataDto<Long> memberIdDto) {
        likeService.like(new LikesRequestDto(id, memberIdDto.data()));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/likes")
    public ResponseEntity<Void> unlike(@PathVariable @Positive Long id, @RequestBody @Valid GenericDataDto<Long> memberIdDto) {
        likeService.unlike(new LikesRequestDto(id, memberIdDto.data()));
        return ResponseEntity.noContent().build();
    }

}
