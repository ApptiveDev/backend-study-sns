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
@RequestMapping("/sns/reply")
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;

    @GetMapping("/{postId}/replies")
    public ResponseEntity<List<ReplyResponse>> getReplies(@PathVariable long postId) {
        List<ReplyResponse> replies = replyService.replyGetAll(postId);
        ResponseEntity<List<ReplyResponse>> responseEntity = new ResponseEntity<>(replies, HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping("/{postId}/add")
    public ResponseEntity<ReplyResponse> writeReply(@PathVariable long postId, @RequestBody ReplyDTO reply) {
        ReplyResponse replyResponse = replyService.writeReply(postId, reply);
        ResponseEntity<ReplyResponse> responseEntity = new ResponseEntity<>(replyResponse, HttpStatus.OK);
        return responseEntity;
    }

    @PutMapping("/{replyId}")
    public ResponseEntity<ReplyResponse> updateReply(@PathVariable long replyId, @RequestBody ReplyDTO reply) {
        ReplyResponse replyResponse = replyService.updateReply(replyId, reply);
        ResponseEntity<ReplyResponse> responseEntity = new ResponseEntity<>(replyResponse, HttpStatus.OK);
        return responseEntity;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReplyResponse> likeReply(@PathVariable long id) {
        ReplyResponse replyResponse = replyService.likeReply(id);
        ResponseEntity<ReplyResponse> responseEntity = new ResponseEntity<>(replyResponse, HttpStatus.OK);
        return responseEntity;
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<String> deleteReply(@PathVariable long replyId) {
        String deleteCheck = replyService.deleteReply(replyId);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(deleteCheck, HttpStatus.OK);
        return responseEntity;
    }
}
