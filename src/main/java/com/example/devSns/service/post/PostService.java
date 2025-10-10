package com.example.devSns.service.post;

import com.example.devSns.domain.post.Post;
import com.example.devSns.domain.post.PostRepository;
import com.example.devSns.web.post.dto.PostDtos.CreateReq;
import com.example.devSns.web.post.dto.PostDtos.UpdateReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Post create(CreateReq req) {
        Post p = Post.builder()
                .content(req.content)
                .username(req.username)
                .likes(0)
                .build();
        return postRepository.save(p);
    }

    @Transactional(readOnly = true)
    public Post get(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Post> list() {
        return postRepository.findAll();
    }

    @Transactional
    public Post update(Long id, UpdateReq req) {
        Post p = get(id);
        p.setContent(req.content);
        return p;
    }

    @Transactional
    public void delete(Long id) {
        Post p = get(id);
        postRepository.delete(p);
    }

    @Transactional
    public Post like(Long id) {
        Post p = get(id);
        p.setLikes(p.getLikes() + 1);
        return p;
    }
}
