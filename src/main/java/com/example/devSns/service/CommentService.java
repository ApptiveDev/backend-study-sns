package com.example.devSns.service;

import com.example.devSns.domain.Comment;
import com.example.devSns.domain.Post;
import com.example.devSns.dto.GenericDataDto;
import com.example.devSns.dto.PaginatedDto;
import com.example.devSns.dto.comment.CommentCreateDto;
import com.example.devSns.dto.comment.CommentResponseDto;
import com.example.devSns.exception.InvalidRequestException;
import com.example.devSns.exception.NotFoundException;
import com.example.devSns.repository.CommentRepository;
import com.example.devSns.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public Long create(CommentCreateDto commentCreateDto) {
        Post post = postRepository.findById(commentCreateDto.post_id())
                .orElseThrow(()->new InvalidRequestException("Invalid Request."));

        Comment comment = Comment.create(
                commentCreateDto.content(),
                post,
                commentCreateDto.userName()
            );

        return commentRepository.save(comment).getId();
    }

    public CommentResponseDto findOne(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(()->new NotFoundException("comment not found"));

        return CommentResponseDto.from(comment);
    }

    @Transactional
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(()->new NotFoundException("comment not found"));
        commentRepository.delete(comment);
    }

    @Transactional
    public CommentResponseDto updateContent(Long id, GenericDataDto<String> contentsDto) {
        if (contentsDto.data() == null || contentsDto.data().isEmpty())
            throw new InvalidRequestException("Invalid Request.");

        Comment comment = commentRepository.findById(id).orElseThrow(()->new NotFoundException("comment not found"));
        comment.setContent(contentsDto.data());

        return findOne(id);
    }

    @Transactional
    public void like(Long id) {
        commentRepository.findById(id).orElseThrow(()->new NotFoundException("comment not found"));
        commentRepository.incrementLikeById(id);
    }

    public PaginatedDto<List<CommentResponseDto>> findAsPaginated(GenericDataDto<Long> idDto, Long postId) {
        Long criteria = idDto.data();
        List<Comment> comments = criteria == null ?
                commentRepository.findTop15ByCreatedAtBeforeAndPostIdOrderByCreatedAtDescPostIdDesc(LocalDateTime.now().plusSeconds(1), postId) :
                commentRepository.findTop15ByIdBeforeAndPostIdOrderByIdDesc(criteria, postId);

        if (comments.isEmpty()) {
            return new PaginatedDto<>(List.of(), null);
        }

        List<CommentResponseDto> commentResponseDtoList = comments
                .stream().map(CommentResponseDto::from).toList();
        Long nextQueryCriteria = comments.getLast().getId();

        return new PaginatedDto<>(commentResponseDtoList, nextQueryCriteria);
    }
}
