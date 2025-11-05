package com.example.devSns.service;

import com.example.devSns.domain.Comment;
import com.example.devSns.dto.GenericDataDto;
import com.example.devSns.dto.PaginatedDto;
import com.example.devSns.dto.comment.CommentCreateDto;
import com.example.devSns.dto.comment.CommentResponseDto;
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

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public Long join(CommentCreateDto commentCreateDto) {
        postRepository.findById(commentCreateDto.post_id())
                .orElseThrow(()->new InvalidRequestException("Invalid Request."));

        Comment comment = new Comment();
        comment.setContent(commentCreateDto.content());
        comment.setPostId(commentCreateDto.post_id());
        comment.setUserName(commentCreateDto.userName());

        return commentRepository.save(comment).getId();
    }

    public CommentResponseDto findOne(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(()->new NotFoundException("comment not found"));

        return CommentResponseDto.from(comment);
    }

    public void delete(Long id) {
        int affectedRows = commentRepository.deleteById(id);
        if (affectedRows == 0)
            throw new NotFoundException("comment not found");
    }

    public CommentResponseDto updateContent(Long id, GenericDataDto<String> contentsDto) {
        if (contentsDto.data() == null) 
            throw new InvalidRequestException("Invalid Request.");

        CommentResponseDto commentResponseDto = findOne(id);
        int affectedRows = commentRepository.updateContentByIdAndUpdatedAt(contentsDto.data(), id, commentResponseDto.updatedAt());
        if (affectedRows == 0)
            throw new RequestConflictException("request conflict.");

        return findOne(id);
    }

    public CommentResponseDto like(Long id) {
        int affectedRows = commentRepository.incrementLikeById(id);
        if (affectedRows == 0)
            throw new NotFoundException("comment not found");

        return findOne(id);
    }

    public PaginatedDto<List<CommentResponseDto>> findAsPaginated(GenericDataDto<LocalDateTime> localDateTimeDto, Long postId) {
        LocalDateTime criteria = localDateTimeDto.data();
        List<Comment> comments = criteria == null ?
                commentRepository.findByBeforeCreatedAtAndPostId(LocalDateTime.now(), postId) :
                commentRepository.findByBeforeCreatedAtAndPostId(criteria, postId);

        if (comments.isEmpty()) {
            return new PaginatedDto<>(List.of(), null);
        }

        List<CommentResponseDto> commentResponseDtoList = comments
                .stream().map(CommentResponseDto::from).toList();
        LocalDateTime nextQueryCriteria = comments.getLast().getCreatedAt();

        return new PaginatedDto<>(commentResponseDtoList, nextQueryCriteria);
    }
}
