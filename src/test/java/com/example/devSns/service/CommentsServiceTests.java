// src/test/java/com/example/devSns/service/CommentServiceTest.java
package com.example.devSns.service;

import com.example.devSns.domain.Comment;
import com.example.devSns.dto.GenericDataDto;
import com.example.devSns.dto.PaginatedDto;
import com.example.devSns.dto.comment.CommentCreateDto;
import com.example.devSns.dto.comment.CommentResponseDto;
import com.example.devSns.exception.InvalidRequestException;
import com.example.devSns.exception.NotFoundException;
import com.example.devSns.repository.CommentRepository;
import com.example.devSns.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    CommentRepository commentRepository;
    PostRepository postRepository;
    CommentService commentService;

    @BeforeEach
    void setUp() {
        commentService = new CommentService(commentRepository, postRepository);
    }

    private static Comment mkComment(
            Long id, Long postId, String content, String userName,
            long likeCount, LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        Comment c = new Comment();
        c.setId(id);
        c.setPostId(postId);
        c.setContent(content);
        c.setUserName(userName);
        c.setLikeCount(likeCount);
        c.setCreatedAt(createdAt);
        c.setUpdatedAt(updatedAt);
        return c;
    }

    @Test
    @DisplayName("join: 댓글 저장 후 생성된 id 반환")
    void join_success() {
        // given
        CommentCreateDto dto = new CommentCreateDto(10L, "hello", "alice");
        Comment saved = mkComment(1L, 10L, "hello", "alice", 0,
                LocalDateTime.now(), null);
        when(commentRepository.save(any(Comment.class))).thenReturn(saved);

        // when
        Long id = commentService.join(dto);

        // then
        assertEquals(1L, id);
        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(captor.capture());
        Comment toSave = captor.getValue();
        assertEquals("hello", toSave.getContent());
        assertEquals("alice", toSave.getUserName());
        assertEquals(10L, toSave.getPostId());
    }

    @Nested
    class FindOne {
        @Test
        @DisplayName("findOne: 존재하면 DTO로 매핑하여 반환")
        void findOne_success() {
            LocalDateTime now = LocalDateTime.now();
            Comment c = mkComment(2L, 10L, "content", "bob", 3, now, null);
            when(commentRepository.findById(2L)).thenReturn(Optional.of(c));

            CommentResponseDto res = commentService.findOne(2L);

            assertEquals(2L, res.id());
            assertEquals(10L, res.post_id());
            assertEquals("content", res.content());
            assertEquals("bob", res.user_name());
            assertEquals(3L, res.like_count());
            assertEquals(now, res.created_at());
        }

        @Test
        @DisplayName("findOne: 없으면 NotFoundException")
        void findOne_notFound() {
            when(commentRepository.findById(99L)).thenReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> commentService.findOne(99L));
        }
    }

    @Nested
    class Delete {
        @Test
        @DisplayName("delete: 삭제 성공(영향 1)")
        void delete_success() {
            when(commentRepository.deleteById(5L)).thenReturn(1);
            assertDoesNotThrow(() -> commentService.delete(5L));
        }

        @Test
        @DisplayName("delete: 영향 0이면 NotFoundException")
        void delete_notFound() {
            when(commentRepository.deleteById(7L)).thenReturn(0);
            assertThrows(NotFoundException.class, () -> commentService.delete(7L));
        }
    }

    @Nested
    class UpdateContent {
        @Test
        @DisplayName("updateContent: 본문 수정 성공")
        void updateContent_success() {
            // given 현재 댓글
            LocalDateTime created = LocalDateTime.now().minusHours(1);
            Comment existing = mkComment(11L, 77L, "old", "cathy", 4, created, null);
            when(commentRepository.findById(11L)).thenReturn(Optional.of(existing));

            // updateById 성공
            when(commentRepository.updateById(any(Comment.class), eq(11L))).thenReturn(1);

            // when
            var res = commentService.updateContent(11L, new GenericDataDto<>("new-content"));

            // then
            assertEquals(11L, res.id());
            assertEquals(77L, res.post_id());
            assertEquals("new-content", res.content());
            assertEquals("cathy", res.user_name());
            assertEquals(4L, res.like_count());
            assertEquals(created, res.created_at()); // 서비스 구현상 created_at은 유지
            verify(commentRepository).updateById(any(Comment.class), eq(11L));
        }

        @Test
        @DisplayName("updateContent: 본문 null이면 InvalidRequestException")
        void updateContent_invalid() {
            assertThrows(InvalidRequestException.class,
                    () -> commentService.updateContent(1L, new GenericDataDto<>(null)));
        }
    }

    @Test
    @DisplayName("like: 좋아요 수 1 증가")
    void like_success() {
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        Comment existing = mkComment(3L, 22L, "hi", "dan", 2, created, null);
        when(commentRepository.findById(3L)).thenReturn(Optional.of(existing));
        when(commentRepository.updateById(any(Comment.class), eq(3L))).thenReturn(1);

        CommentResponseDto res = commentService.like(3L);

        assertEquals(3L, res.id());
        assertEquals(3L, res.like_count()); // 2 -> 3
        verify(commentRepository).updateById(any(Comment.class), eq(3L));
    }

    @Nested
    class FindAsPaginated {
        @Test
        @DisplayName("findAsPaginated: 결과가 있으면 DTO 리스트와 nextQueryCriteria 반환")
        void paginated_hasData() {
            Long postId = 55L;
            LocalDateTime t1 = LocalDateTime.now().minusMinutes(30);
            LocalDateTime t2 = LocalDateTime.now().minusMinutes(40);
            Comment c1 = mkComment(101L, postId, "a", "u1", 0, t1, null);
            Comment c2 = mkComment(102L, postId, "b", "u2", 1, t2, null);

            when(commentRepository.findByBeforeCreatedAtAndPostId(any(LocalDateTime.class), eq(postId)))
                    .thenReturn(List.of(c1, c2));

            PaginatedDto<List<CommentResponseDto>> page =
                    commentService.findAsPaginated(new GenericDataDto<>(null), postId);

            assertNotNull(page.data());
            assertEquals(2, page.data().size());
            assertEquals(t2, page.nextQueryCriteria()); // 마지막 요소의 createdAt
        }

        @Test
        @DisplayName("findAsPaginated: 결과가 없으면 빈 리스트와 next=null")
        void paginated_empty() {
            when(commentRepository.findByBeforeCreatedAtAndPostId(any(LocalDateTime.class), eq(99L)))
                    .thenReturn(List.of());

            PaginatedDto<List<CommentResponseDto>> page =
                    commentService.findAsPaginated(new GenericDataDto<>(null), 99L);

            assertNotNull(page.data());
            assertTrue(page.data().isEmpty());
            assertNull(page.nextQueryCriteria());
        }
    }
}
