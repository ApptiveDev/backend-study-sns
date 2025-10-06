package services;

import dto.Post;
import dto.PostResponse;
import entities.Posts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.PostRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private PostRepository postRepository;

    public Posts dtoToEntity(Post post) {
        Posts postEntity = new Posts().builder()
                .username(post.username())
                .content(post.content())
                .createat(LocalDateTime.now())
                .build();
        return postEntity;
    }

    public PostResponse entityToDto(Posts post) {
        return PostResponse.builder()
                .username(post.getUsername())
                .content(post.getContent())
                .like(post.getLikeit())
                .createAt(post.getCreateat())
                .updateAt(post.getUpdateat())
                .build();
    }

    @Transactional
    public PostResponse save(Post post) {
        Posts postEntity = dtoToEntity(post);
        postRepository.save(postEntity);
        return entityToDto(postEntity);
    }

    @Transactional
    public List<PostResponse> findAll() {
        List<Posts> posts = postRepository.findAll();
        List<PostResponse> postResponses = new ArrayList<>();
        for (Posts post : posts) {
            postResponses.add(entityToDto(post));
        }
        return postResponses;
    }

    @Transactional
    public PostResponse findByUsername(String username) {
        Posts post = postRepository.findByUsername(username);
        return entityToDto(post);
    }

    @Transactional
    public PostResponse update(Post post) {
        Posts postEntity = dtoToEntity(post);
        postEntity.setUpdateat(LocalDateTime.now());
        postRepository.save(postEntity);
        return entityToDto(postEntity);
    }

    @Transactional
    public void delete(Post post) {
        postRepository.delete(dtoToEntity(post));
    }
}
