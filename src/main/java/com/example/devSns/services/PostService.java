package com.example.devSns.services;

import com.example.devSns.dto.PostDTO;
import com.example.devSns.dto.PostResponse;
import com.example.devSns.entities.Posts;
import com.example.devSns.entities.Users;
import com.example.devSns.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.devSns.repositories.PostRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.devSns.dto.PostDTO.dtoToEntity;
import static com.example.devSns.dto.PostResponse.entityToDto;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional // 트랜잭션 보장
    public PostResponse save(PostDTO postDTO) {
        Users user = userRepository.findById(postDTO.userId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
        Posts postEntity = dtoToEntity(postDTO, user);
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
    public List<PostResponse> findByUserID(Long userID) {
        Users user = userRepository.findById(userID).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));// 작성자 기준 post 조회
        List<Posts> postsByName = postRepository.findByUsers(user);
        List<PostResponse> postResponses = new ArrayList<>();
        for (Posts post : postsByName) {
            postResponses.add(entityToDto(post));
        }
        return postResponses;
    }

    @Transactional
    public List<PostResponse> findByContent(String content) {
        List<Posts> postsByContent = postRepository.findByContentContaining(content);
        List<PostResponse> postResponses = new ArrayList<>();
        for (Posts post : postsByContent) {
            postResponses.add(entityToDto(post));
        }
        return postResponses;
    }

    @Transactional
    public PostResponse update(Long id, PostDTO postDTO) { // 수정된 post 반영
        Posts postEntity = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("수정하려는 Post를 찾을 수 없습니다."));
        postEntity.setContent(postDTO.content());
        postEntity.setUpdateat(LocalDateTime.now());
        return entityToDto(postEntity);
    }

    @Transactional
    public PostResponse likePost(Long id) {
        Posts postEntity = postRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        postEntity.setLikeit(postEntity.getLikeit() + 1);
        return entityToDto(postEntity);
    }

    @Transactional
    public void delete(Long id) { // post 삭제
        Posts object = postRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("삭제하려는 Post를 찾을 수 없습니다."));
        postRepository.delete(object);
    }
}
