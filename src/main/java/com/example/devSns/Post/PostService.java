package com.example.devSns.Post;

import com.example.devSns.Post.Dto.AddPostRequestDto;
import com.example.devSns.Post.Dto.GetPostResponseDto;
import com.example.devSns.Post.Dto.UpdatePostRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    public void createPost(AddPostRequestDto Dto) {
        Post post = new Post(
                null,
                Dto.content(),
                0L,
                Dto.username(),
                LocalDateTime.now(),
                null
        );
        postRepository.save(post);
    }

    public GetPostResponseDto findById(Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));

        return new GetPostResponseDto(
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

    public void updatePost(Long id , UpdatePostRequestDto Dto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() ->new EntityNotFoundException("게시글이 존재하지 않습니다"));

        post.Update(Dto);
        postRepository.save(post); // JDBC에서는 Update 쿼리가 나오는 메소드로 사용했지만 변경감지로 처리
    }

}
