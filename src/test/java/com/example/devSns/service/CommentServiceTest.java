package com.example.devSns.service;

import com.example.devSns.domain.Comment;
import com.example.devSns.domain.Post;
import com.example.devSns.dto.CommentRequestDto;
import com.example.devSns.repository.CommentRepository;
import com.example.devSns.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Mockito 사용
class CommentServiceTest {

    @Mock // 가짜 Repository 만들기
    private CommentRepository commentRepository;

    @Mock // 가짜 Repository 만들기
    private PostRepository postRepository;

    @InjectMocks // @Mock을 @InjectMocks에 주입
    private CommentService commentService;

    @DisplayName("대댓글을 성공적으로 생성한다.")
    @Test
    void createReplyComment() {
        // 1. Given (준비)
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent("대댓글입니다.");
        requestDto.setPostId(1L);
        requestDto.setParentId(10L); // 부모 댓글 ID 10

        Post mockPost = new Post("테스트", "내용");
        Comment mockParentComment = new Comment(mockPost, null, "부모 댓글");

        // "postRepository.findById(1L)이 호출되면, 가짜 Post를 반환해라"
        when(postRepository.findById(1L)).thenReturn(Optional.of(mockPost));

        // "commentRepository.findById(10L)이 호출되면, 가짜 부모 Comment를 반환해라"
        when(commentRepository.findById(10L)).thenReturn(Optional.of(mockParentComment));

        // 2. When (실행)
        commentService.createComment(requestDto);

        // 3. Then (검증)
        // "commentRepository.save() 메서드가 1번 호출되었는지 검증"
        // (즉, 저장이 잘 되었는지)
        verify(commentRepository, times(1)).save(any(Comment.class));
    }
}