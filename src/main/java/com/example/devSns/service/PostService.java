package com.example.devSns.service;

import com.example.devSns.domain.Post;
import com.example.devSns.dto.GenericDataDto;
import com.example.devSns.dto.PaginatedDto;
import com.example.devSns.dto.post.PostCreateDto;
import com.example.devSns.dto.post.PostResponseDto;
import com.example.devSns.exception.InvalidRequestException;
import com.example.devSns.exception.NotFoundException;
import com.example.devSns.repository.JpaCommentRepository;
import com.example.devSns.repository.JpaPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class PostService {

//    private final PostRepository postRepository;
    private final JpaPostRepository postRepository;
    private final JpaCommentRepository commentRepository;

    @Autowired
    public PostService(JpaPostRepository postRepository, JpaCommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Long join(PostCreateDto postCreateDto) {
        Post post = Post.create(postCreateDto.content(), postCreateDto.userName());
        postRepository.save(post);
        return post.getId();
    }

    public PostResponseDto findOne(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new NotFoundException("post not found"));
        Long comments = commentRepository.countCommentsByPostId(id);

        return PostResponseDto.from(post, comments);
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new NotFoundException("post not found"));
        postRepository.delete(post);
    }

    @Transactional
    public PostResponseDto updateContent(Long id, GenericDataDto<String> contentsDto) {
        if (contentsDto.data() == null || contentsDto.data().isEmpty())
            throw new InvalidRequestException("Invalid request.");

        Post post = postRepository.findById(id).orElseThrow(()->new NotFoundException("post not found"));
        post.setContent(contentsDto.data());

        return findOne(id);
    }

    @Transactional
    public void like(Long id) {
        postRepository.findById(id).orElseThrow(()->new NotFoundException("post not found"));
        postRepository.incrementLikeById(id);
    }


    public PaginatedDto<List<PostResponseDto>> findAsPaginated(GenericDataDto<LocalDateTime> localDateTimeDto) {
        LocalDateTime criteria = localDateTimeDto.data();
        List<Post> posts = criteria == null ?
                postRepository.findTop15ByCreatedAtBeforeOrderByCreatedAtDesc(LocalDateTime.now()) :
                postRepository.findTop15ByCreatedAtBeforeOrderByCreatedAtDesc(criteria);

        if (posts.isEmpty()) {
            return new PaginatedDto<>(List.of(), null);
        }
//        List<Long> postIds = posts.stream().map(Post::getId).toList();
        Map<Long, Long> commentMapping = new HashMap<>();
        commentRepository.countCommentsAndGroupByPostIdIn(posts)
                .forEach(o -> {commentMapping.put(o[0], o[1]);});


        List<PostResponseDto> postResponseDtoList = posts.stream()
                .map((p)->PostResponseDto.from(p, commentMapping.get(p.getId())))
                .toList();

        LocalDateTime nextQueryCriteria = posts.getLast().getCreatedAt();
        return new PaginatedDto<>(postResponseDtoList, nextQueryCriteria);
    }



}
