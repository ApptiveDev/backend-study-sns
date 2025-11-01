package com.example.devSns.services;

import com.example.devSns.dto.ReplyDTO;
import com.example.devSns.dto.ReplyResponse;
import com.example.devSns.repositories.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;

    @Transactional
    public List<ReplyResponse> replyGetAll(@PathVariable long postId) {

    }

    @Transactional
    public ReplyResponse writeReply(@PathVariable long postId, ReplyDTO reply) {

    }
}
