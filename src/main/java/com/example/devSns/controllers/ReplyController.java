package com.example.devSns.controllers;

import com.example.devSns.dto.ReplyDTO;
import com.example.devSns.dto.ReplyResponse;
import com.example.devSns.services.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;

    @GetMapping("/{postId}/replies")
    public ResponseEntity<List<ReplyResponse>> getReplies(@PathVariable long postId) {
        List<ReplyResponse> replies = replyService.replyGetAll(postId);
        ResponseEntity<List<ReplyResponse>> responseEntity = new ResponseEntity<>(replies, HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping("/{postId}")
    public ResponseEntity<ReplyResponse> writeReply(@PathVariable long postId, long userId, ReplyDTO reply) {
        ReplyResponse replyResponse = replyService.writeReply(postId, userId, reply);
        ResponseEntity<ReplyResponse> responseEntity = new ResponseEntity<>(replyResponse, HttpStatus.OK);
        return responseEntity;
    }

    @PatchMapping("/{postid}")
    public ResponseEntity<ReplyResponse> updateReply(@PathVariable long postId, long userId, ReplyDTO reply) {
        ReplyResponse replyResponse = replyService.updateReply(postId, userId, reply);
        ResponseEntity<ReplyResponse> responseEntity = new ResponseEntity<>(replyResponse, HttpStatus.OK);
        return responseEntity;
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deleteReply(@PathVariable long postId, long userId) {
        String deleteCheck = replyService.deleteReply(postId, userId);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(deleteCheck, HttpStatus.OK);
        return responseEntity;
    }
}
