package com.example.devSns.service;

import com.example.devSns.domain.Post;
import com.example.devSns.dto.GenericDataDto;
import com.example.devSns.dto.PaginatedDto;
import com.example.devSns.dto.post.PostCreateDto;
import com.example.devSns.dto.post.PostResponseDto;
import com.example.devSns.exception.InvalidRequestException;
import com.example.devSns.exception.NotFoundException;
import com.example.devSns.repository.CommentRepository;
import com.example.devSns.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTests {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("게시글 생성 성공")
    void join_success() {
        // given
        PostCreateDto createDto = new PostCreateDto("Test Content", "testUser");
        Post post = Post.create("Test Content", "testUser");
        post.setId(1L); // Mocking the ID set after save

        when(postRepository.save(any(Post.class))).thenReturn(post);

        // when
        Long postId = postService.join(createDto);

        // then
        assertNotNull(postId);
        assertEquals(1L, postId);
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("ID로 게시글 단건 조회 성공")
    void findOne_success() {
        // given
        Long postId = 1L;
        Post post = createDummyPost(postId, "Test Content", "testUser");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.countCommentsByPostId(postId)).thenReturn(5L);

        // when
        PostResponseDto responseDto = postService.findOne(postId);

        // then
        assertNotNull(responseDto);
        assertEquals(postId, responseDto.id());
        assertEquals("Test Content", responseDto.content());
        assertEquals("testUser", responseDto.userName());
        assertEquals(5L, responseDto.comments());
    }

    @Test
    @DisplayName("존재하지 않는 ID로 게시글 조회 시 NotFoundException 발생")
    void findOne_throwsNotFoundException() {
        // given
        Long postId = 99L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () -> postService.findOne(postId));
        verify(commentRepository, never()).countCommentsByPostId(anyLong());
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    void delete_success() {
        // given
        Long postId = 1L;
        Post post = createDummyPost(postId, "Content", "User");
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // when
        postService.delete(postId);

        // then
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).delete(post);
    }

    @Test
    @DisplayName("존재하지 않는 게시글 삭제 시 NotFoundException 발생")
    void delete_throwsNotFoundException() {
        // given
        Long postId = 99L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () -> postService.delete(postId));
        verify(postRepository, never()).delete(any(Post.class));
    }


    @Test
    @DisplayName("게시글 내용 수정 성공")
    void updateContent_success() {
        // given
        Long postId = 1L;
        String newContent = "Updated Content";
        GenericDataDto<String> contentDto = new GenericDataDto<>(newContent);
        Post post = createDummyPost(postId, "Original Content", "testUser");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.countCommentsByPostId(postId)).thenReturn(0L);

        // when
        PostResponseDto responseDto = postService.updateContent(postId, contentDto);

        // then
        assertEquals(newContent, post.getContent()); // Verify side-effect
        assertEquals(newContent, responseDto.content());
        verify(postRepository, times(2)).findById(postId); // findById is called in updateContent and findOne
    }

    @Test
    @DisplayName("게시글 내용 수정 시 내용이 비어있으면 InvalidRequestException 발생")
    void updateContent_throwsInvalidRequestException_whenContentIsEmpty() {
        // given
        Long postId = 1L;
        GenericDataDto<String> contentDto = new GenericDataDto<>("");

        // when & then
        assertThrows(InvalidRequestException.class, () -> postService.updateContent(postId, contentDto));
    }

    @Test
    @DisplayName("게시글 좋아요 성공")
    void like_success() {
        // given
        Long postId = 1L;
        Post post = createDummyPost(postId, "Content", "User");
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // when
        postService.like(postId);

        // then
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).incrementLikeById(postId);
    }

    @Test
    @DisplayName("존재하지 않는 게시글에 좋아요 시 NotFoundException 발생")
    void like_throwsNotFoundException() {
        // given
        Long postId = 99L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () -> postService.like(postId));
        verify(postRepository, never()).incrementLikeById(anyLong());
    }

    @Test
    @DisplayName("게시글 페이지네이션 조회 - 첫 페이지")
    void findAsPaginated_initialPage() {
        // given
        List<Post> posts = List.of(
                createDummyPost(10L, "Content 10", "User"),
                createDummyPost(9L, "Content 9", "User")
        );
        when(postRepository.findTop15ByCreatedAtBeforeOrderByCreatedAtDesc(any(LocalDateTime.class))).thenReturn(posts);
        when(commentRepository.countCommentsAndGroupByPostIdIn(posts)).thenReturn(
                List.of(new Long[]{10L, 5L}, new Long[]{9L, 2L})
        );

        // when
        PaginatedDto<List<PostResponseDto>> result = postService.findAsPaginated(new GenericDataDto<>(null));

        // then
        assertEquals(2, result.data().size());
        assertEquals(9L, result.nextQueryCriteria());
        assertEquals(5L, result.data().get(0).comments());
        assertEquals(2L, result.data().get(1).comments());
    }

    @Test
    @DisplayName("게시글 페이지네이션 조회 - 다음 페이지")
    void findAsPaginated_nextPage() {
        // given
        Long beforeId = 11L;
        List<Post> posts = List.of(
                createDummyPost(10L, "Content 10", "User"),
                createDummyPost(9L, "Content 9", "User")
        );
        when(postRepository.findTop15ByIdBeforeOrderByIdDesc(beforeId)).thenReturn(posts);
        when(commentRepository.countCommentsAndGroupByPostIdIn(posts)).thenReturn(
                List.of(new Long[]{10L, 5L}, new Long[]{9L, 2L})
        );

        // when
        PaginatedDto<List<PostResponseDto>> result = postService.findAsPaginated(new GenericDataDto<>(beforeId));

        // then
        assertEquals(2, result.data().size());
        assertEquals(9L, result.nextQueryCriteria()); // last element id
    }

    @Test
    @DisplayName("게시글 페이지네이션 조회 - 결과 없음")
    void findAsPaginated_noResults() {
        // given
        when(postRepository.findTop15ByCreatedAtBeforeOrderByCreatedAtDesc(any(LocalDateTime.class))).thenReturn(Collections.emptyList());

        // when
        PaginatedDto<List<PostResponseDto>> result = postService.findAsPaginated(new GenericDataDto<>(null));

        // then
        assertTrue(result.data().isEmpty());
        assertNull(result.nextQueryCriteria());
    }

    private Post createDummyPost(Long id, String content, String userName) {
        Post post = Post.create(content, userName);
        post.setId(id);
        return post;
    }
}