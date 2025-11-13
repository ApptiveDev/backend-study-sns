package com.example.devSns.service;

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
    public List<CommentEntity> getCommentsByPost(Long postId) {
        PostEntity postEntity = postRepository.findById(postId).orElseThrow();
        return postEntity.getComments();
    }

    // 댓글 생성
    @Transactional
    public CommentEntity createComment(Long postId, CommentEntity comment) {
        PostEntity postEntity = postRepository.findById(postId).orElseThrow();
        comment.setPost(postEntity);
        return commentRepository.save(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
