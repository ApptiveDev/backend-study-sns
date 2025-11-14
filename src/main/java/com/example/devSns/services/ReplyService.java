package com.example.devSns.services;

import com.example.devSns.dto.ReplyDTO;
import com.example.devSns.dto.ReplyResponse;
import com.example.devSns.entities.Posts;
import com.example.devSns.entities.Replies;
import com.example.devSns.entities.Users;
import com.example.devSns.repositories.PostRepository;
import com.example.devSns.repositories.ReplyRepository;
import com.example.devSns.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ReplyResponse> replyGetAll(@PathVariable long postId) {
        Posts post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        List<Replies> replies = replyRepository.findByPosts(post);
        List<ReplyResponse> repliesResponse = new ArrayList<>();
        for (Replies reply:  replies) {
            repliesResponse.add(ReplyResponse.entityToDTO(reply));
        }
        return repliesResponse;
    }

    @Transactional
    public ReplyResponse writeReply(@PathVariable long postId, ReplyDTO reply) {
        Posts post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        Users user = userRepository.findById(reply.userID()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
        Replies replyEntity = ReplyDTO.dtoToEntity(post, user, reply);
        replyRepository.save(replyEntity);
        return ReplyResponse.entityToDTO(replyEntity);
    }

    @Transactional
    public ReplyResponse likeReply(@PathVariable long replyId) {
        Replies replyEntity = replyRepository.findById(replyId);
        replyEntity.setLikeit(replyEntity.getLikeit() + 1);
        replyRepository.save(replyEntity);
        return ReplyResponse.entityToDTO(replyEntity);
    }

    @Transactional
    public ReplyResponse updateReply (@PathVariable long replyId, ReplyDTO reply) {
        Users user = userRepository.findById(reply.userID()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
        if(user.getId() != reply.userID()) {
            throw new SecurityException();
        }
        Replies replyEntity = replyRepository.findById(replyId);
        replyEntity.setReply(reply.comment());
        replyEntity.setUpdateAt(LocalDateTime.now());
        replyRepository.save(replyEntity);
        return ReplyResponse.entityToDTO(replyEntity);
    }

    @Transactional
    public String deleteReply(@PathVariable long replyId) {
        Replies replyEntity = replyRepository.findById(replyId);
        replyRepository.delete(replyEntity);
        return "성공적으로 삭제되었습니다";
    }
}
