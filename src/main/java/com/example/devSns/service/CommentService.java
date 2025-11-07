package com.example.devSns.service;

import com.example.devSns.domain.Comment;
import com.example.devSns.domain.CommentRepository;
import com.example.devSns.domain.Post;
import com.example.devSns.domain.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepo;
    private final PostRepository postRepo;

    public CommentService(CommentRepository commentRepo, PostRepository postRepo) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
    }

    public List<Comment> list(Long postId) {
        ensurePostExists(postId);
        return commentRepo.findByPostIdOrderByIdAsc(postId);
    }

    public Comment get(Long postId, Long commentId) {
        return commentRepo.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
    }

    public Comment create(Long postId, String content) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        Comment comment = Comment.builder().content(content).post(post).build();
        return commentRepo.save(comment);
    }

    public Comment update(Long postId, Long commentId, String content) {
        Comment comment = get(postId, commentId);
        comment.update(content);
        return commentRepo.save(comment);
    }

    public void delete(Long postId, Long commentId) {
        Comment comment = get(postId, commentId);
        commentRepo.delete(comment);
    }

    private void ensurePostExists(Long postId) {
        if (!postRepo.existsById(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
    }
}
