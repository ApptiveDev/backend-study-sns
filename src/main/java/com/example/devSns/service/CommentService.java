package com.example.devSns.service;

import com.example.devSns.domain.Comment;
import com.example.devSns.domain.Member;
import com.example.devSns.domain.Post;
import com.example.devSns.dto.CommentRequestDto;
import com.example.devSns.dto.CommentResponseDto;
import com.example.devSns.dto.CommentUpdateDto; // DTO import
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
    private final PostRepository postRepository;
    private final MemberService memberService; // Member 조회용

    // 1. 댓글 생성
    @Transactional
    public Comment createComment(CommentRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        Member member = memberService.getMemberById(requestDto.getMemberId());

        Comment parent = null;
        if (requestDto.getParentId() != null) {
            parent = commentRepository.findById(requestDto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
        }

        Comment comment = new Comment(post, parent, requestDto.getContent(), member);
        return commentRepository.save(comment);
    }

    // 2. 특정 게시글의 모든 댓글 조회 (대댓글 구조 포함)
    public List<CommentResponseDto> getCommentsByPost(Long postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        List<Comment> topLevelComments = commentRepository.findByPostIdAndParentIsNull(postId);

        return topLevelComments.stream()
                .map(CommentResponseDto::from)
                .collect(Collectors.toList());
    }

    // 3. 댓글 수정
    @Transactional
    public Comment updateComment(Long id, CommentUpdateDto updateDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        // 권한 확인
        if (!comment.getMember().getId().equals(updateDto.getMemberId())) {
            throw new IllegalArgumentException("You are not authorized to update this comment.");
        }

        comment.update(updateDto.getContent());
        return commentRepository.save(comment);
    }

    // 4. 댓글 삭제
    @Transactional
    public void deleteComment(Long id, Long memberId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        // 권한 확인
        if (!comment.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("You are not authorized to delete this comment.");
        }

        commentRepository.delete(comment);
    }
}