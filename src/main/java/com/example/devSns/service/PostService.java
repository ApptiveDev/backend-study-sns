package com.example.devSns.service;

import com.example.devSns.domain.Post;
import com.example.devSns.domain.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PostService {

    private final PostRepository repo;

    public PostService(PostRepository repo) {
        this.repo = repo;
    }

    public List<Post> findAll() {
        return repo.findAll();
    }

    public Post findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    public Post create(String title, String content) {
        Post post = Post.builder()
                .title(title)
                .content(content)
                .build();
        return repo.save(post);
    }

    public Post update(Long id, String title, String content) {
        Post post = findById(id);
        post.update(title, content);
        return repo.save(post);
    }

    public void delete(Long id) {
        Post post = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        repo.delete(post); // 연관 댓글까지 안전하게 제거
    }
}
