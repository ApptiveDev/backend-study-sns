package com.example.devSns.app.domain.utils;

import com.example.devSns.app.domain.model.Post;
import com.example.devSns.app.domain.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 글이 없습니다: " + id));
    }

    public Post createPost(String content, String authorName) {
        Post newPost = new Post(null, content, authorName, 0, null, null);
        return postRepository.save(newPost);
    }

    public Post updatePost(Long id, String newContent) {
        Post existing = getPost(id);
        Post updated = existing.withContent(newContent);
        return postRepository.save(updated);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public Post likePost(Long id) {
        Post existing = getPost(id);
        Post liked = existing.withLikes(existing.getLikes() + 1);
        return postRepository.save(liked);
    }
}
