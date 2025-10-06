package com.example.devSns.service;

import com.example.devSns.domain.Comment;
import com.example.devSns.dto.GenericDataDto;
import com.example.devSns.dto.PaginatedDto;
import com.example.devSns.dto.comment.CommentCreateDto;
import com.example.devSns.dto.comment.CommentResponseDto;
import com.example.devSns.dto.post.PostResponseDto;
import com.example.devSns.exception.InvalidRequestException;
import com.example.devSns.exception.NotFoundException;
import com.example.devSns.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Long join(CommentCreateDto commentCreateDto) {
        Comment comment = new Comment();
        comment.setContent(commentCreateDto.content());
        comment.setPostId(commentCreateDto.post_id());
        comment.setUserName(commentCreateDto.user_name());

        return commentRepository.save(comment).getId();
    }

    public CommentResponseDto findOne(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(()->new NotFoundException("comment not found"));

        return CommentResponseDto.from(comment);
    }

    public void delete(Long id) {
        int affectedRows = commentRepository.deleteById(id);
        if (affectedRows == 0) throw new NotFoundException("comment not found");
    }

    private CommentResponseDto update(CommentResponseDto commentResponseDto) {
        Comment comment = new Comment();
        comment.setId(commentResponseDto.id());
        comment.setPostId(commentResponseDto.post_id());
        comment.setContent(commentResponseDto.content());
        comment.setUserName(commentResponseDto.user_name());
        comment.setLikeCount(commentResponseDto.like_count());
        comment.setCreatedAt(commentResponseDto.created_at());
        comment.setUpdatedAt(commentResponseDto.updated_at());
        int affectedRows = commentRepository.updateById(comment, commentResponseDto.id());
        if (affectedRows == 0) throw new NotFoundException("comment not found");
        return CommentResponseDto.from(comment);
    }

    public CommentResponseDto updateContent(Long id, GenericDataDto<String> contentsDto) {
        if (contentsDto.data() == null) throw new InvalidRequestException("Invalid Request.");
        CommentResponseDto commentResponseDto = findOne(id);

        return update(new CommentResponseDto(
                commentResponseDto.id(),
                commentResponseDto.post_id(),
                contentsDto.data(),
                commentResponseDto.user_name(),
                commentResponseDto.like_count(),
                commentResponseDto.created_at(),
                commentResponseDto.updated_at()
        ));
    }

    public CommentResponseDto like(Long id) {
        CommentResponseDto commentResponseDto = findOne(id);

        return update(new CommentResponseDto(
                commentResponseDto.id(),
                commentResponseDto.post_id(),
                commentResponseDto.content(),
                commentResponseDto.user_name(),
                commentResponseDto.like_count() + 1,
                commentResponseDto.created_at(),
                commentResponseDto.updated_at()
        ));
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
