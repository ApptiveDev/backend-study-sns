package com.example.devSns.service;

import com.example.devSns.dto.CommentRequest;
import com.example.devSns.dto.CommentResponse;
import com.example.devSns.entity.CommentEntity;
import com.example.devSns.entity.PostEntity;
import com.example.devSns.repository.CommentRepository;
import com.example.devSns.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    // 특정 게시글 댓글 조회
    public List<CommentResponse> getCommentsByPost(Long postId) {
        PostEntity postEntity = postRepository.findById(postId).orElseThrow();
        return postEntity.getComments().stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getUsername(),
                        comment.getContent(),
                        comment.getCreatedAt().toString()
                ))
                .toList();
    }

    // 댓글 생성
    @Transactional
    public CommentResponse createComment(Long postId, CommentRequest request) {
        PostEntity postEntity = postRepository.findById(postId).orElseThrow();
        CommentEntity comment = CommentEntity.create(
                postEntity,
                request.getUsername(),
                request.getContent()
        );

        commentRepository.save(comment);

        return new CommentResponse(
                comment.getId(),
                comment.getUsername(),
                comment.getContent(),
                comment.getCreatedAt().toString()
        );
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
