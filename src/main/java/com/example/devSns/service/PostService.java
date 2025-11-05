package com.example.devSns.service;

import com.example.devSns.domain.Comment;
import com.example.devSns.domain.Post;
import com.example.devSns.dto.GenericDataDto;
import com.example.devSns.dto.PaginatedDto;
import com.example.devSns.dto.post.PostCreateDto;
import com.example.devSns.dto.post.PostResponseDto;
import com.example.devSns.exception.InvalidRequestException;
import com.example.devSns.exception.NotFoundException;
import com.example.devSns.exception.RequestConflictException;
import com.example.devSns.repository.CommentRepository;
import com.example.devSns.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public PostService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Long join(PostCreateDto postCreateDto) {
        Post post = new Post();
        post.setContent(postCreateDto.content());
        post.setUserName(postCreateDto.userName());
        postRepository.save(post);
        return post.getId();
    }

    public PostResponseDto findOne(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new NotFoundException("post not found"));
        List<Comment> comments = commentRepository.findByBeforeCreatedAtAndPostId(LocalDateTime.now(), id );

        return PostResponseDto.from(post, comments);
    }

    @Transactional
    public void delete(Long id) {
        int affectedRows = postRepository.deleteById(id);
        if (affectedRows == 0) 
            throw new NotFoundException("post not found");
    }

    @Transactional
    public PostResponseDto updateContent(Long id, GenericDataDto<String> contentsDto) {
        if (contentsDto.data() == null) 
            throw new InvalidRequestException("Invalid request.");

        PostResponseDto postResponseDto = findOne(id);
        int affectedRows = postRepository.updateContentByIdAndUpdatedAt(contentsDto.data(), id, postResponseDto.updatedAt());
        
        if (affectedRows == 0) 
            throw new RequestConflictException("request conflict.");

        return findOne(id);
    }

    @Transactional
    public PostResponseDto like(Long id) {
        int affectedRows = postRepository.incrementLikeById(id);
        if (affectedRows == 0) 
            throw new NotFoundException("post not found");

        return findOne(id);
    }


    public PaginatedDto<List<PostResponseDto>> findAsPaginated(GenericDataDto<LocalDateTime> localDateTimeDto) {
        LocalDateTime criteria = localDateTimeDto.data();
        List<Post> posts = criteria == null ?
                postRepository.findByBeforeCreatedAt(LocalDateTime.now()) :
                postRepository.findByBeforeCreatedAt(criteria);

        if (posts.isEmpty()) {
            return new PaginatedDto<>(List.of(), null);
        }
        List<Long> postIds = posts.stream().map(Post::getId).toList();
        Map<Long, List<Comment>> commentMapping = commentRepository.findAndGroupByPostIds(postIds);


        List<PostResponseDto> postResponseDtoList = posts.
                stream().map((p)->PostResponseDto.from(p, commentMapping.get(p.getId()))).toList();
        LocalDateTime nextQueryCriteria = posts.getLast().getCreatedAt();

        return new PaginatedDto<>(postResponseDtoList, nextQueryCriteria);
    }



}
