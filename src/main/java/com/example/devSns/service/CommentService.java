package com.example.devSns.service;

import com.example.devSns.dto.CommentCreateRequest;
import com.example.devSns.entity.Comment;
import com.example.devSns.entity.Member;
import com.example.devSns.entity.Post;
import com.example.devSns.repository.CommentRepository;
import com.example.devSns.repository.MemberRepository;
import com.example.devSns.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentService {
    private final CommentRepository CommentRepository;
    private final PostRepository PostRepository;
    private final MemberRepository MemberRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, MemberRepository memberRepository) {
        this.CommentRepository = commentRepository;
        this.PostRepository = postRepository;
        this.MemberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public List<Comment> GetCommentByPost(Long postId){
        return CommentRepository.FindByPostId(postId);
    }

    @Transactional
    public Comment AddComment(Long postId, CommentCreateRequest request){
        Post post = PostRepository.findById(postId).orElseThrow(()-> new IllegalArgumentException("post not found"));
        Member member = MemberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .username(request.getUsername())
                .post(post)
                .build();
        post.AddComment(comment);
        comment.AssignMember(member);
        return CommentRepository.save(comment);
    }
    public Comment UpdateComment(Long commentId, String newContent){
        Comment comment = CommentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("comment not found"));
        comment.Update(newContent);
        return CommentRepository.save(comment);
    }

    public void deleteComment(Long id){
        CommentRepository.deleteById(id);
    }
}