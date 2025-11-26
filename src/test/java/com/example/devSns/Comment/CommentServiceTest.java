package com.example.devSns.Comment;

import com.example.devSns.Comment.Dto.CreateCommentDto;
import com.example.devSns.Comment.Dto.UpdateCommentDto;
import com.example.devSns.Post.Post;
import com.example.devSns.Post.PostRepository;
import com.example.devSns.global.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    @DisplayName("댓글 생성 성공 - setter 없이")
    void createComment_success_noSetter() {
        // --- Post 엔티티 생성 ---
        Post post = new Post("content", "writer", 0L); // id는 null
        // post id는 setter 없이 흉내: doAnswer에서 사용

        // --- DTO 생성 ---
        CreateCommentDto dto = new CreateCommentDto("좋은 글이에요!", "author");

        // --- Mock 정의 ---
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // save 호출 시 id를 흉내내고 post 할당
        doAnswer(invocation -> {
            Comment saved = invocation.getArgument(0);
            // id 직접 세팅 불가 → 그냥 반환 객체로 테스트
            // post 필드만 Mockito verify 등에서 확인 가능
            return saved;
        }).when(commentRepository).save(any(Comment.class));

        // --- 테스트 실행 ---
        commentService.createComment(1L, dto);

        // --- 검증 ---
        verify(postRepository).findById(1L);
        verify(commentRepository).save(argThat(comment -> comment.getComment().equals("좋은 글이에요!")
                && comment.getAuthor().equals("author")
                && comment.getPost() == post // Post 할당 확인
        ));
    }


    @Test
    @DisplayName("댓글 생성 실패 - 존재하지 않는 게시글")
    void createComment_postNotFound() {
        CreateCommentDto dto = new CreateCommentDto("좋아요!", "author");

        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.createComment(1L, dto));

        verify(postRepository).findById(1L);
        verify(commentRepository, never()).save(any());
    }

    @Test
    @DisplayName("댓글 수정 성공 - setter 없이")
    void updateComment_success_noSetter() {
        // --- 기존 Comment 생성 (id 없음) ---
        Comment comment = new Comment("원본 댓글", "author");
        // id는 null, setter 없음

        UpdateCommentDto dto = new UpdateCommentDto("수정 댓글", "author2");

        // --- Mock 정의 ---
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // 저장 후 그대로 반환

        // --- 테스트 실행 ---
        Comment updated = commentService.updateComment(1L, dto);

        // --- 검증 ---
        assertEquals("수정 댓글", updated.getComment());
        assertEquals("author2", updated.getAuthor());

        verify(commentRepository).findById(1L);
        verify(commentRepository).save(comment);
    }


    @Test
    @DisplayName("댓글 삭제")
    void deleteCommentById_success() {
        commentService.deleteCommentById(1L);

        verify(commentRepository).deleteById(1L);
    }

    @Test
    @DisplayName("답글 댓글 생성 성공 - setter 없이")
    void createReplyComment_success() {
        Post post = new Post("content", "writer", 0L); // id는 null
        Comment parent = new Comment("원본 댓글", "author");

        CreateCommentDto replyDto = new CreateCommentDto("답글 댓글", "replyAuthor");

        // --- Mock 정의 ---
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(parent));
        doAnswer(invocation -> invocation.getArgument(0))
                .when(commentRepository).save(any(Comment.class));

        // --- 테스트 실행 ---
        commentService.createReplyComment(1L, 1L, replyDto);

        // --- 검증 ---
        verify(postRepository).findById(1L);
        verify(commentRepository).findById(1L);
        verify(commentRepository).save(argThat(reply ->
                reply.getComment().equals("답글 댓글") &&
                        reply.getAuthor().equals("replyAuthor") &&
                        reply.getParent() == parent &&
                        parent.getChildren().contains(reply)
        ));
    }

    @Test
    @DisplayName("댓글 목록 조회 성공")
    void getAllComments_success() {
        Comment c1 = new Comment("댓글1", "author1");
        Comment c2 = new Comment("댓글2", "author2");
        List<Comment> comments = new ArrayList<>();
        comments.add(c1);
        comments.add(c2);

        when(commentRepository.findByPostIdAndParentIsNull(1L)).thenReturn(comments);

        List<Comment> result = commentService.getAllComments(1L);

        assertEquals(2, result.size());
        assertTrue(result.contains(c1));
        assertTrue(result.contains(c2));
        verify(commentRepository).findByPostIdAndParentIsNull(1L);
    }

    @Test
    @DisplayName("특정 댓글 조회 성공")
    void getCommentById_success() {
        Comment comment = new Comment("댓글 내용", "author");

        when(commentRepository.findByPostIdAndId(1L, 1L)).thenReturn(Optional.of(comment));

        Comment result = commentService.getCommentById(1L, 1L);

        assertEquals("댓글 내용", result.getComment());
        assertEquals("author", result.getAuthor());

        verify(commentRepository).findByPostIdAndId(1L, 1L);
    }

    @Test
    @DisplayName("특정 댓글 조회 실패 - 존재하지 않는 댓글")
    void getCommentById_notFound() {
        when(commentRepository.findByPostIdAndId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> commentService.getCommentById(1L, 1L));

        verify(commentRepository).findByPostIdAndId(1L, 1L);
    }
}

