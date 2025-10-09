package com.example.devSns.Post;

import com.example.devSns.Post.DTO.AddPostRequestDTO;
import com.example.devSns.Post.DTO.GetPostResponseDTO;
import com.example.devSns.Post.DTO.UpdatePostRequestDTO;
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

    public GetPostResponseDTO findById(Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));

        return new GetPostResponseDTO(
                post.getContent(),
                post.getLikeCount(),
                post.getUsername(),
                post.getCreatedAt()
        );
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
