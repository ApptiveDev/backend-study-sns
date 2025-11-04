package com.example.devSns.services;

import com.example.devSns.dto.ReplyDTO;
import com.example.devSns.dto.ReplyResponse;
import com.example.devSns.entities.Posts;
import com.example.devSns.entities.Replies;
import com.example.devSns.entities.Users;
import com.example.devSns.repositories.PostRepository;
import com.example.devSns.repositories.ReplyRepository;
import com.example.devSns.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<ReplyResponse> replyGetAll(@PathVariable long postId) {
        Posts post = postRepository.findById(postId).orElseThrow();
        List<Replies> replies = replyRepository.findByPosts(post);
        List<ReplyResponse> repliesResponse = new ArrayList<>();
        for (Replies reply:  replies) {
            repliesResponse.add(ReplyResponse.entityToDTO(reply));
        }
        return repliesResponse;
    }

    @Transactional
    public ReplyResponse writeReply(@PathVariable long postId, long userId, ReplyDTO reply) {
        Posts post = postRepository.findById(postId).orElseThrow();
        Users user = userRepository.findById(userId);
        Replies replyEntity = ReplyDTO.dtoToEntity(post, user, reply);
        replyRepository.save(replyEntity);
        return ReplyResponse.entityToDTO(replyEntity);
    }

    @Transactional
    public ReplyResponse updateReply(@PathVariable long postId, long userId, ReplyDTO reply) {
        Posts post = postRepository.findById(postId).orElseThrow();
        Users user = userRepository.findById(userId);
        Replies replyEntity = ReplyDTO.dtoToEntity(post, user, reply);
        replyRepository.save(replyEntity);
        return ReplyResponse.entityToDTO(replyEntity);
    }

    @Transactional
    public String deleteReply(@PathVariable long postId, long userId) {
        Posts post = postRepository.findById(postId).orElseThrow();
        Users user = userRepository.findById(userId);
        Replies replyEntity = replyRepository.findById(postId).orElseThrow();
        replyRepository.delete(replyEntity);
        return "성공적으로 삭제되었습니다";
    }
}
