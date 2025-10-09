package com.example.devSns.app.data.jpa.adapter;

import com.example.devSns.app.data.jpa.entity.PostEntity;
import com.example.devSns.app.data.jpa.repository.PostJpaRepository;
import com.example.devSns.app.domain.mapper.PostMapper;
import com.example.devSns.app.domain.model.Post;
import com.example.devSns.app.domain.repository.PostRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository postJpaRepository;

    public PostRepositoryImpl(PostJpaRepository postJpaRepository) {
        this.postJpaRepository = postJpaRepository;
    }

    @Override
    public Post save(Post post) {
        PostEntity entity = PostMapper.toEntity(post);
        PostEntity saved = postJpaRepository.save(entity);
        return PostMapper.toDomain(saved);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postJpaRepository.findById(id)
                .map(PostMapper::toDomain);
    }

    @Override
    public List<Post> findAll() {
        return postJpaRepository.findAll().stream()
                .map(PostMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        postJpaRepository.deleteById(id);
    }
}
