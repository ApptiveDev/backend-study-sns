package com.example.devSns.service;

import com.example.devSns.dto.CommentCreateRequest;
import com.example.devSns.entity.Comment;
import com.example.devSns.entity.Member;
import com.example.devSns.entity.Post;
import com.example.devSns.repository.CommentRepository;
import com.example.devSns.repository.MemberRepository;
import com.example.devSns.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentByPost(Long postId){
        return commentRepository.findByPost_Id(postId);
    }

    @Transactional
    public Comment addComment(Long postId, CommentCreateRequest request){
        Post post = postRepository.findById(postId).orElseThrow(()-> new IllegalArgumentException("post not found"));
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .username(request.getUsername())
                .post(post)
                .build();
        post.addComment(comment);
        comment.assignMember(member);
        return commentRepository.save(comment);
    }
    public Comment updateComment(Long commentId, String newContent){
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("comment not found"));
        comment.update(newContent);
        return commentRepository.save(comment);
    }

    public void deleteComment(Long id){
        commentRepository.deleteById(id);
    }
}