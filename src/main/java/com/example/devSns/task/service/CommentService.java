package com.example.devSns.task.service;

import com.example.devSns.task.dto.CommentCreateDto;
import com.example.devSns.task.dto.CommentResponseDto;
import com.example.devSns.task.dto.CommentUpdateDto;
import com.example.devSns.task.entity.Comment;
import com.example.devSns.task.entity.Post;
import com.example.devSns.task.repository.CommentRepository;
import com.example.devSns.task.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository){
        this.commentRepository = commentRepository;
        this.postRepository  = postRepository;
    }

    // 댓글 달기. 특정 게시글 밑에 작성.
    @Transactional
    public void createComment(Long id, CommentCreateDto dto){
        Post post = getPostById(id);
        Comment comment = Comment.of(dto,post);
        commentRepository.save(comment);
    }

    // 댓글 목록 조회 - 특정 포스트 밑에 모든 댓글이 조회되도록
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getAllComments(Long postId){
        return commentRepository.findAlignedCommentByPostId(postId)
                .stream()
                .map(CommentResponseDto::new)
                .toList();
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long id, CommentUpdateDto dto){
        Comment comment = getCommentById(id);
        comment.update(dto);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long id){
        commentRepository.deleteById(id);
    }

    private Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + id));
    }

    private Comment getCommentById(Long id){
        return commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다"));
    }
}
