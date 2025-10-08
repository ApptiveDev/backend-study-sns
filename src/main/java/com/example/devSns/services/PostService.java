package com.example.devSns.services;

import com.example.devSns.dto.PostDTO;
import com.example.devSns.dto.PostResponse;
import com.example.devSns.entities.Posts;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.devSns.repositories.PostRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Posts dtoToEntity(PostDTO postDTO) {
        Posts postEntity = Posts.builder()
                .username(postDTO.username())
                .content(postDTO.content())
                .build();
        return postEntity;
    }

    public PostResponse entityToDto(Posts post) {
        return PostResponse.builder()
                .id(post.getId())
                .username(post.getUsername())
                .content(post.getContent())
                .like(post.getLikeit())
                .createAt(post.getCreateat())
                .updateAt(post.getUpdateat())
                .build();
    }

    @Transactional // 트랜잭션 보장
    public PostResponse save(PostDTO postDTO) { // post insert
        Posts postEntity = dtoToEntity(postDTO);
        postEntity.setCreateat(LocalDateTime.now());
        Posts resultEntity = postRepository.save(postEntity);
        return entityToDto(resultEntity);
    }

    @Transactional
    public List<PostResponse> findAll() { // 전체 post 조회
        List<Posts> posts = postRepository.findAll();
        List<PostResponse> postResponses = new ArrayList<>();
        for (Posts post : posts) {
            postResponses.add(entityToDto(post));
        }
        return postResponses;
    }

    @Transactional
    public List<PostResponse> findByUsername(String username) { // 작성자 기준 post 조회
        List<Posts> postsByName = postRepository.findByUsername(username);
        List<PostResponse> postResponses = new ArrayList<>();
        for (Posts post : postsByName) {
            postResponses.add(entityToDto(post));
        }
        return postResponses;
    }

    @Transactional
    public PostResponse update(PostDTO postDTO) { // 수정된 post 반영
        Posts postEntity = postRepository.findById(postDTO.id())
                .orElseThrow(() -> new EntityNotFoundException("수정하려는 Post를 찾을 수 없습니다."));
        postEntity.setContent(postDTO.content());
        postEntity.setUpdateat(LocalDateTime.now());
        return entityToDto(postEntity);
    }

    @Transactional
    public void delete(PostDTO postDTO) { // post 삭제
        Posts object = postRepository.findById(postDTO.id())
                        .orElseThrow(() -> new EntityNotFoundException("삭제하려는 Post를 찾을 수 없습니다."));
        postRepository.delete(object);
    }
}
