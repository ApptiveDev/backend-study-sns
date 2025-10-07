package com.example.devSns.service;

import com.example.devSns.entity.PostEntity;
import com.example.devSns.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    // 생성자
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostEntity> getAllPosts() {
        return postRepository.findAll();
    }

    public PostEntity getPost(Long id) {
        return postRepository.findById(id).orElseThrow();
    }

    @Transactional
    public PostEntity createPost(PostEntity postEntity) {
        return postRepository.save(postEntity);
    }

    @Transactional
    public PostEntity updatePost(Long id, PostEntity updated) {
        PostEntity postEntity = postRepository.findById(id).orElseThrow();
        postEntity.setTitle(updated.getTitle());
        postEntity.setContent(updated.getContent());
        return postRepository.save(postEntity);
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

}