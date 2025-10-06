package com.example.devSns.service;

import com.example.devSns.domain.Comment;
import com.example.devSns.domain.Post;
import com.example.devSns.dto.GenericDataDto;
import com.example.devSns.dto.PaginatedDto;
import com.example.devSns.dto.post.PostCreateDto;
import com.example.devSns.dto.post.PostResponseDto;
import com.example.devSns.exception.NotFoundException;
import com.example.devSns.repository.CommentRepository;
import com.example.devSns.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public Long join(PostCreateDto postCreateDto) {
        Post post = new Post();
        post.setContent(postCreateDto.content());
        post.setUserName(postCreateDto.user_name());
        postRepository.save(post);
        return post.getId();
    }

    public PostResponseDto findOne(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new NotFoundException("post not found"));
        List<Comment> comments = commentRepository.findByBeforeCreatedAtAndPostId(LocalDateTime.now(), id );

        return PostResponseDto.from(post, comments);
    }

    public void delete(Long id) {
        int affectedRows = postRepository.deleteById(id);
    }

    public PaginatedDto<List<PostResponseDto>> findAsPaginated(GenericDataDto<LocalDateTime> localDateTimeDto) {
        LocalDateTime criteria = localDateTimeDto.data();
        List<Post> posts = criteria == null ?
                postRepository.findByBeforeCreatedAt(LocalDateTime.now()) :
                postRepository.findByBeforeCreatedAt(criteria);

        List<Long> postIds = posts.stream().map(Post::getId).toList();
        Map<Long, List<Comment>> commentMapping = commentRepository.findAndGroupByPostIds(postIds);


        List<PostResponseDto> postResponseDtoList = posts.
                stream().map((p)->PostResponseDto.from(p, commentMapping.get(p.getId()))).toList();
        LocalDateTime nextQueryCriteria = posts.getLast().getCreatedAt();

        return new PaginatedDto<>(postResponseDtoList, nextQueryCriteria);
    }



}
