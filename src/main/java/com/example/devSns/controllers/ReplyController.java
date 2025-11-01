package com.example.devSns.controllers;

import com.example.devSns.dto.ReplyDTO;
import com.example.devSns.dto.ReplyResponse;
import com.example.devSns.services.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<ReplyResponse>> getComments(@PathVariable long postId) {
        return null;
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<List<ReplyResponse>> writeComment(@PathVariable long postId, ReplyDTO comment) {
        return null;
    }


}
