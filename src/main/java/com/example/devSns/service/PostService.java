package com.example.devSns.service;

import com.example.devSns.entity.Post;
import com.example.devSns.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAll(){
        return postRepository.findAll();
    }

    public Optional<Post> findById(Long id){
        return postRepository.findById(id);
    }

    public Post save(Post post){
        return postRepository.save(post);
    }
    public Post updatePost(Long id, Post updatedPost) {
        Post existingPost = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));

        updatedPost.setCreatedAt(existingPost.getCreatedAt());
        updatedPost.setUpdatedAt(LocalDateTime.now());

        updatedPost.setId(id);

        return postRepository.save(updatedPost);
    }

    public void delete(Long id){
        postRepository.deleteById(id);
    }
}