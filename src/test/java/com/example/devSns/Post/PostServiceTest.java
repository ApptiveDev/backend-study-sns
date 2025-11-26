package com.example.devSns.Post;

import com.example.devSns.Comment.Comment;
import com.example.devSns.Comment.CommentRepository;
import com.example.devSns.Heart.HeartRepository;
import com.example.devSns.Member.Member;
import com.example.devSns.Member.MemberRepository;
import com.example.devSns.Post.Dto.AddPostRequestDto;
import com.example.devSns.Post.Dto.GetPostResponseDto;
import com.example.devSns.Post.Dto.UpdatePostRequestDto;
import com.example.devSns.global.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private HeartRepository heartRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("게시글 생성 성공 - Member가 존재할 때")
    void createPost_success() {
        // given
        AddPostRequestDto dto = new AddPostRequestDto("내용입니다", "작성자");
        Long memberId = 1L;

        Member mockMember = new Member();
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(mockMember));

        // when
        postService.createPost(dto, memberId);

        // then
        verify(memberRepository).findById(memberId);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("게시글 생성 실패 - Member가 존재하지 않을 때 예외 발생")
    void createPost_fail_memberNotFound() {
        // given
        AddPostRequestDto dto = new AddPostRequestDto("내용", "작성자");
        Long memberId = 99L;

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(EntityNotFoundException.class, () -> postService.createPost(dto, memberId));

        verify(memberRepository).findById(memberId);
        verify(postRepository, never()).save(any());
    }

    @Test
    @DisplayName("게시글 단건 조회 성공 - 존재하는 게시글일 때")
    void findById_success() {
        // given
        Long postId = 1L;
        Post mockPost = new Post("테스트 내용", "작성자", 5L);
        ReflectionTestUtils.setField(mockPost, "id", postId); // id 필드 수동 세팅 (private일 경우)

        // 댓글 리스트 Mocking
        List<Comment> mockComments = List.of(new Comment(), new Comment());

        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));
        when(commentRepository.findByPostIdAndParentIsNull(postId)).thenReturn(mockComments);

        // when
        System.out.println("실제 findById 호출됨!");
        GetPostResponseDto result = postService.findById(postId);

        // then
        verify(postRepository).findById(postId);
        verify(commentRepository).findByPostIdAndParentIsNull(postId);

        assertEquals("테스트 내용", result.content());
        assertEquals("작성자", result.username());
        assertEquals(5L, result.likeCount());
        assertEquals(mockComments, result.commnetList());
    }
    @Test
    @DisplayName("게시글 단건 조회 실패 - 존재하지 않는 게시글일 때 예외 발생")
    void findById_fail_notFound() {
        // given
        Long postId = 999L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(EntityNotFoundException.class,
                () -> postService.findById(postId));

        verify(postRepository).findById(postId);
        verify(commentRepository, never()).findByPostIdAndParentIsNull(any());
    }
    @Test
    @DisplayName("전체 게시글 조회 성공 - 모든 게시글 DTO로 변환")
    void findAll_success() throws Exception {
        // given
        Post post1 = new Post("내용1", "작성자1", 3L);
        Post post2 = new Post("내용2", "작성자2", 7L);

        // 🔧 Reflection으로 ID 수동 세팅
        Field idField = Post.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(post1, 1L);
        idField.set(post2, 2L);

        List<Post> mockPosts = List.of(post1, post2);

        when(postRepository.findAll()).thenReturn(mockPosts);
        when(commentRepository.findByPostIdAndParentIsNull(anyLong()))
                .thenReturn(List.of());

        // when
        List<GetPostResponseDto> result = postService.findAll();

        // then
        verify(postRepository).findAll();
        verify(commentRepository, times(2)).findByPostIdAndParentIsNull(anyLong());

        assertEquals(2, result.size());
        assertEquals("내용1", result.get(0).content());
        assertEquals("내용2", result.get(1).content());
    }
    @Test
    @DisplayName("좋아요 수 계산 - 모든 게시글의 좋아요 갯수를 갱신")
    void countLikes_success() {
        // given
        Post post1 = new Post("내용1", "작성자1", 0L);
        Post post2 = new Post("내용2", "작성자2", 0L);
        ReflectionTestUtils.setField(post1, "id", 1L);
        ReflectionTestUtils.setField(post2, "id", 2L);

        List<Post> posts = List.of(post1, post2);

        when(postRepository.findAll()).thenReturn(posts);
        when(heartRepository.countByPostIdAndLiked(1L, true)).thenReturn(5L);
        when(heartRepository.countByPostIdAndLiked(2L, true)).thenReturn(3L);

        // when
        postService.countLikes();

        // then
        verify(postRepository).findAll();
        verify(heartRepository, times(2)).countByPostIdAndLiked(anyLong(), eq(true));

        assertEquals(5L, post1.getLikeCount());
        assertEquals(3L, post2.getLikeCount());
    }


    @Test
    @DisplayName("게시글 삭제 성공 - 존재하는 게시글일 때")
    void deletePost_success() {
        // given
        Long postId = 1L;
        Post mockPost = new Post("내용", "작성자", 5L);

        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));

        // quick debug line — 테스트 실행 중 콘솔에 찍히면 메서드가 호출되는지 확인 가능
        // (필요시 제거)
        System.out.println(">>> running deletePost_success test");

        // when
        postService.delete(postId);

        // then
        verify(postRepository).findById(postId);
        verify(postRepository).delete(mockPost);
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 존재하지 않는 게시글일 때")
    void deletePost_fail_notFound() {
        // given
        Long postId = 999L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        System.out.println(">>> running deletePost_fail_notFound test");

        // when & then
        assertThrows(EntityNotFoundException.class,
                () -> postService.delete(postId));

        verify(postRepository).findById(postId);
        verify(postRepository, never()).delete(any());
    }

    @Test
    @DisplayName("게시글 수정 성공 - 실제 객체의 필드 값이 변경됨")
    void updatePost_success() {
        // given
        Long postId = 1L;
        Post post = new Post("기존 내용", "기존 작성자", 0L);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        UpdatePostRequestDto dto = new UpdatePostRequestDto("수정된 내용", "수정된 작성자");

        // when
        postService.updatePost(postId, dto);

        // then
        verify(postRepository).findById(postId);
        verify(postRepository).save(post);

        // ✅ 실제 필드 변경 여부 검증 (커버리지에 영향 줌)
        assertEquals("수정된 내용", post.getContent());
        assertEquals("수정된 작성자", post.getUserName());
    }

    @Test
    @DisplayName("게시글 수정 실패 - 존재하지 않는 게시글일 때 예외 발생")
    void updatePost_notFound() {
        // given
        Long postId = 999L;
        UpdatePostRequestDto dto = new UpdatePostRequestDto("수정된 내용", "작성자");
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // when & then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> postService.updatePost(postId, dto)
        );

        assertEquals("게시글이 존재하지 않습니다", exception.getMessage());
        verify(postRepository).findById(postId);
        verify(postRepository, never()).save(any());
    }
}





