package com.example.devSns.controller;


import com.example.devSns.dto.GenericDataDto;
import com.example.devSns.dto.follow.FollowRequestDto;
import com.example.devSns.service.FollowsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/members")
public class FollowsController {

    private final FollowsService followsService;
    public FollowsController(FollowsService followsService) {
        this.followsService = followsService;
    }

    @PostMapping("/{id}/follower")
    public ResponseEntity<Void> follow(@PathVariable Long id, @RequestBody @Valid GenericDataDto<Long> followerIdDto) {
        Long followId = followsService.follow(new FollowRequestDto(id, followerIdDto.data()));

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(followId)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}/follower")
    public ResponseEntity<Void> unfollow(@PathVariable Long id, @RequestBody @Valid GenericDataDto<Long> followerIdDto) {
        followsService.unfollow(new FollowRequestDto(id, followerIdDto.data()));
        return ResponseEntity.noContent().build();
    }

}
