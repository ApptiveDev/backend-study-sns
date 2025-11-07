package com.example.devSns.service;

import com.example.devSns.entity.Comment;
import com.example.devSns.entity.Post;
import com.example.devSns.repository.CommentRepository;
import com.example.devSns.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentByPost(Long postId){
        return commentRepository.findByPost_Id(postId);
    }

    public Comment addComment(Long postId, Comment comment){
        Post post = postRepository.findById(postId).orElseThrow(()-> new IllegalArgumentException("post not found"));

        post.addComment(comment);
        postRepository.save(post);
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