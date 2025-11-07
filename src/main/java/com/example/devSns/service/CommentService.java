package com.example.devSns.service;

import com.example.devSns.domain.Comment;
import com.example.devSns.domain.Post;
import com.example.devSns.dto.CommentRequestDto;
import com.example.devSns.dto.CommentResponseDto;
import com.example.devSns.repository.CommentRepository;
import com.example.devSns.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository; // PostService의 getPostById를 써도 됩니다.

    // 1. 댓글 생성
    @Transactional
    public Comment createComment(CommentRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        Comment parent = null;
        // parentId가 null이 아니면 (즉, 대댓글이면)
        if (requestDto.getParentId() != null) {
            parent = commentRepository.findById(requestDto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
        }

        Comment comment = new Comment(post, parent, requestDto.getContent());
        return commentRepository.save(comment);
    }

    // 2. 특정 게시글의 모든 댓글 조회 (대댓글 구조 포함)
    public List<CommentResponseDto> getCommentsByPost(Long postId) {
        // 게시글이 존재하는지 확인 (없으면 PostService의 getPostById처럼 예외 처리)
        postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // 1. 해당 게시글의 "최상위 댓글"만 찾는다.
        List<Comment> topLevelComments = commentRepository.findByPostIdAndParentIsNull(postId);

        // 2. 최상위 댓글들을 DTO로 변환 (CommentResponseDto.from이 재귀적으로 대댓글도 처리)
        return topLevelComments.stream()
                .map(CommentResponseDto::from)
                .collect(Collectors.toList());
    }

    // 3. 댓글 수정
    @Transactional
    public Comment updateComment(Long id, String content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        comment.update(content); // 엔티티의 update 메서드 사용
        return commentRepository.save(comment);
    }

    // 4. 댓글 삭제
    @Transactional
    public void deleteComment(Long id) {
        // 댓글 존재 확인
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        // CaskadeType.REMOVE와 orphanRemoval=true 덕분에
        // 이 댓글(과 그 자식 댓글들)이 삭제됨
        commentRepository.delete(comment);
    }
}