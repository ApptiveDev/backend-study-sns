package com.example.devSns.service;

import com.example.devSns.domain.Comment;
import com.example.devSns.domain.Post;
import com.example.devSns.dto.GenericDataDto;
import com.example.devSns.dto.PaginatedDto;
import com.example.devSns.dto.post.PostCreateDto;
import com.example.devSns.dto.post.PostResponseDto;
import com.example.devSns.exception.InvalidRequestException;
import com.example.devSns.exception.NotFoundException;
import com.example.devSns.repository.CommentRepository;
import com.example.devSns.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTests {

    @Mock PostRepository postRepository;
    @Mock CommentRepository commentRepository;
    @InjectMocks PostService postService;

    @Captor ArgumentCaptor<Post> postCaptor;

    @BeforeEach
    void setUp() { }

    // --- join ---
    @Test
    void join_저장후_id반환() {
        PostCreateDto dto = new PostCreateDto("hello", "alice");

        // save가 post.id를 채워주는 동작 흉내
        doAnswer(inv -> { Post p = inv.getArgument(0); p.setId(10L); return null; })
                .when(postRepository).save(any(Post.class));

        Long id = postService.join(dto);

        assertThat(id).isEqualTo(10L);
        verify(postRepository).save(postCaptor.capture());
        Post saved = postCaptor.getValue();
        assertThat(saved.getContent()).isEqualTo("hello");
        assertThat(saved.getUserName()).isEqualTo("alice");
    }

    // --- findOne ---
    @Test
    void findOne_정상조회_댓글포함() {
        Long id = 1L;
        Post p = post(1L, "p1", "bob",
                LocalDateTime.of(2025,10,1,12,0),
                LocalDateTime.of(2025,10,1,12,5));
        List<Comment> cs = List.of(
                comment(11L, 1L, "c1", "u1", 3L, LocalDateTime.of(2025,10,1,12,10))
        );

        when(postRepository.findById(1L)).thenReturn(Optional.of(p));
        when(commentRepository.findByBeforeCreatedAtAndPostId(any(LocalDateTime.class), eq(1L)))
                .thenReturn(cs);

        PostResponseDto dto = postService.findOne(1L);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.content()).isEqualTo("p1");
        assertThat(dto.comments()).hasSize(1);
        verify(postRepository).findById(1L);
        verify(commentRepository).findByBeforeCreatedAtAndPostId(any(LocalDateTime.class), eq(1L));
    }

    @Test
    void findOne_없으면_NotFound() {
        when(postRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> postService.findOne(999L));
    }

    // --- delete ---
    @Test
    void delete_성공() {
        when(postRepository.deleteById(5L)).thenReturn(1);
        postService.delete(5L);
        verify(postRepository).deleteById(5L);
    }

    @Test
    void delete_영향0_NotFound() {
        when(postRepository.deleteById(5L)).thenReturn(0);
        assertThrows(NotFoundException.class, () -> postService.delete(5L));
    }

    // --- updateContent ---
    @Test
    void updateContent_null이면_InvalidRequest() {
        assertThrows(InvalidRequestException.class,
                () -> postService.updateContent(1L, new GenericDataDto<>(null)));
    }

    @Test
    void updateContent_정상_내용반영() {
        Long id = 7L;
        Post base = post(id, "old", "alice",
                LocalDateTime.of(2025,10,1,10,0),
                LocalDateTime.of(2025,10,1,10,5));
        when(postRepository.findById(id)).thenReturn(Optional.of(base));
        when(commentRepository.findByBeforeCreatedAtAndPostId(any(LocalDateTime.class), eq(id)))
                .thenReturn(List.of()); // findOne 내부 호출
        when(postRepository.updateById(any(Post.class), eq(id))).thenReturn(1);

        PostResponseDto res = postService.updateContent(id, new GenericDataDto<>("new-content"));

        assertThat(res.id()).isEqualTo(id);
        assertThat(res.content()).isEqualTo("new-content");
        // updateById로 넘어간 Post 검증
        verify(postRepository).updateById(postCaptor.capture(), eq(id));
        Post upd = postCaptor.getValue();
        assertThat(upd.getId()).isEqualTo(id);
        assertThat(upd.getContent()).isEqualTo("new-content");
        assertThat(upd.getUserName()).isEqualTo("alice");
    }

    @Test
    void updateContent_update영향0_NotFound() {
        Long id = 7L;
        Post base = post(id, "old", "alice",
                LocalDateTime.of(2025,10,1,10,0),
                LocalDateTime.of(2025,10,1,10,5));
        when(postRepository.findById(id)).thenReturn(Optional.of(base));
        when(commentRepository.findByBeforeCreatedAtAndPostId(any(LocalDateTime.class), eq(id)))
                .thenReturn(List.of());
        when(postRepository.updateById(any(Post.class), eq(id))).thenReturn(0);

        assertThrows(NotFoundException.class,
                () -> postService.updateContent(id, new GenericDataDto<>("new")));
    }

    // --- like ---
    @Test
    void like_정상_likeCount증가() {
        Long id = 3L;
        Post base = post(id, "p", "kim",
                LocalDateTime.of(2025,10,1,9,0),
                LocalDateTime.of(2025,10,1,9,5));
        base.setLikeCount(10L);

        when(postRepository.findById(id)).thenReturn(Optional.of(base));
        when(commentRepository.findByBeforeCreatedAtAndPostId(any(LocalDateTime.class), eq(id)))
                .thenReturn(List.of());
        when(postRepository.updateById(any(Post.class), eq(id))).thenReturn(1);

        PostResponseDto res = postService.like(id);

        assertThat(res.id()).isEqualTo(id);
        assertThat(res.likeCount()).isEqualTo(11L);
        verify(postRepository).updateById(postCaptor.capture(), eq(id));
        assertThat(postCaptor.getValue().getLikeCount()).isEqualTo(11L);
    }

    // --- findAsPaginated ---
    @Test
    void findAsPaginated_기준null_now로조회_댓글그룹핑_다음커서마지막createdAt() {
        Post p1 = post(1L, "p1", "a",
                LocalDateTime.of(2025,10,1,10,0),
                LocalDateTime.of(2025,10,1,10,5));
        Post p2 = post(2L, "p2", "b",
                LocalDateTime.of(2025,10,1,9,0),
                LocalDateTime.of(2025,10,1,9,5));
        List<Post> list = List.of(p1, p2);

        when(postRepository.findByBeforeCreatedAt(any(LocalDateTime.class))).thenReturn(list);
        when(commentRepository.findAndGroupByPostIds(List.of(1L, 2L)))
                .thenReturn(Map.of(
                        1L, List.of(comment(11L, 1L, "c1", "u1", 0L, LocalDateTime.of(2025,10,1,10,10))),
                        2L, List.of()
                ));

        PaginatedDto<List<PostResponseDto>> page = postService.findAsPaginated(new GenericDataDto<>(null));

        assertThat(page.data()).hasSize(2);
        assertThat(page.data().get(0).id()).isEqualTo(1L);
        assertThat(page.data().get(0).comments()).hasSize(1);
        assertThat(page.data().get(1).id()).isEqualTo(2L);
        assertThat(page.data().get(1).comments()).isEmpty();
        assertThat(page.nextQueryCriteria()).isEqualTo(p2.getCreatedAt()); // 마지막 포스트의 createdAt
    }

    @Test
    void findAsPaginated_기준있으면_그값사용_빈결과면_next_null() {
        LocalDateTime criteria = LocalDateTime.of(2025,10,1,12,0);
        when(postRepository.findByBeforeCreatedAt(criteria)).thenReturn(List.of());

        PaginatedDto<List<PostResponseDto>> page =
                postService.findAsPaginated(new GenericDataDto<>(criteria));

        assertThat(page.data()).isEmpty();
        assertThat(page.nextQueryCriteria()).isNull();
        verify(postRepository).findByBeforeCreatedAt(criteria);
        verify(commentRepository, never()).findAndGroupByPostIds(anyList());
    }

    // ===== helpers =====
    private static Post post(Long id, String content, String user, LocalDateTime created, LocalDateTime updated) {
        Post p = new Post();
        p.setId(id);
        p.setContent(content);
        p.setUserName(user);
        p.setLikeCount(0L);
        p.setCreatedAt(created);
        p.setUpdatedAt(updated);
        return p;
    }

    private static Comment comment(Long id, Long postId, String content,
                                   String userName, Long likeCount, LocalDateTime created) {
        Comment c = new Comment();
        c.setId(id);
        c.setPostId(postId);
        c.setContent(content);
        c.setUserName(userName);
        c.setLikeCount(likeCount);
        c.setCreatedAt(created);
        return c;
    }
}
