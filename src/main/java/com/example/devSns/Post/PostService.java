package com.example.devSns.Post;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    public void createPost(AddPostRequestDTO DTO) {
        Post post = new Post(
                null,
                DTO.content(),
                0L,
                DTO.username(),
                LocalDateTime.now(),
                null
        );
        postRepository.save(post);
    }

    public Post findById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public void delete(Long id) {
        postRepository.delete(id);
    }

    public void update(Post post) {
        postRepository.save(post);
    }

}
