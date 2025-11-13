package com.example.devSns.Member;

import com.example.devSns.Heart.Heart;
import com.example.devSns.Heart.HeartRepository;
import com.example.devSns.Heart.LikeStatus;
import com.example.devSns.Member.Dto.SignMemberRequestDto;
import com.example.devSns.Post.Post;
import com.example.devSns.Post.PostRepository;
import com.example.devSns.Post.PostService;
import com.example.devSns.Post.Dto.GetPostResponseDto;
import com.example.devSns.global.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PostService postService;

    @Mock
    private HeartRepository heartRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private MemberService memberService;

    // -------------------- 회원 생성 --------------------
    @Test
    @DisplayName("회원 생성 성공")
    void createMember_success() {
        SignMemberRequestDto dto = new SignMemberRequestDto("nickname", "email", "password", Gender.MALE, 25);
        Member savedMember = dto.toEntity();
        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        var result = memberService.createMember(dto);

        assertEquals(savedMember.getNickname(), result.nickname());
        verify(memberRepository).save(any(Member.class));
    }

    // -------------------- 특정 멤버 조회 --------------------
    @Test
    @DisplayName("회원 조회 성공")
    void getMemberById_success() {
        Member member = new Member("nickname", "email", "password", Gender.FEMALE, 30);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        var result = memberService.getMemberById(1L);

        assertEquals(member.getNickname(), result.nickname());
        verify(memberRepository).findById(1L);
    }

    @Test
    @DisplayName("회원 조회 실패 - 존재하지 않는 경우")
    void getMemberById_notFound() {
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> memberService.getMemberById(99L));

        verify(memberRepository).findById(99L);
    }

    // -------------------- 회원 글+댓글 조회 --------------------
    @Test
    @DisplayName("회원 게시글 + 댓글 조회 성공")
    void getMemberPostAndComment_success() {
        // --- 테스트용 Member 생성 ---
        Member member = new Member("nickname", "email", "password", Gender.MALE, 28);

        // 게시글 추가 (id는 null)
        Post post1 = new Post("content1", "writer1", 0L);
        Post post2 = new Post("content2", "writer2", 0L);
        member.getPosts().add(post1);
        member.getPosts().add(post2);

        // --- Mock 동작 정의 ---
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        // doAnswer + any() 사용: id가 null이어도 안전하게 반환
        doAnswer(invocation -> {
            Long postId = invocation.getArgument(0);
            if (postId == null) postId = 0L; // null 처리
            return new GetPostResponseDto("content" + postId, postId, "writer", null, new ArrayList<>());
        }).when(postService).findById(any());

        // --- 테스트 실행 ---
        var result = memberService.getMemberPostAndComment(1L);

        // --- 검증 ---
        assertEquals("nickname", result.nickname());
        assertEquals(2, result.post().size());

        verify(memberRepository).findById(1L);
        verify(postService, times(2)).findById(any());
    }


    @Test
    @DisplayName("회원 게시글 + 댓글 조회 실패 - 멤버 없음")
    void getMemberPostAndComment_notFound() {
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> memberService.getMemberPostAndComment(99L));

        verify(memberRepository).findById(99L);
        verifyNoInteractions(postService);
    }

    // -------------------- 좋아요 토글 --------------------
    @Test
    @DisplayName("좋아요 토글 - 새로운 Heart 생성 후 LIKE로 변경")
    void convertLikeStatus_newHeart() {
        Post post = new Post("content", "writer", 0L);
        Member member = new Member("nickname", "email", "password", Gender.MALE, 22);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(heartRepository.existsByPostIdAndMemberId(1L, 1L)).thenReturn(false);

        memberService.convertLikeStatus(1L, 1L);

        verify(heartRepository).save(any(Heart.class));
    }

    @Test
    @DisplayName("좋아요 토글 - 기존 Heart 조회 후 토글")
    void convertLikeStatus_existingHeart() {
        Post post = new Post("content", "writer", 0L);
        Member member = new Member("nickname", "email", "password", Gender.FEMALE, 22);
        Heart heart = new Heart(post, member, LikeStatus.NONE);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(heartRepository.existsByPostIdAndMemberId(1L, 1L)).thenReturn(true);
        when(heartRepository.findByPostIdAndMemberId(1L, 1L)).thenReturn(Optional.of(heart));

        memberService.convertLikeStatus(1L, 1L);

        assertEquals(LikeStatus.LIKE, heart.getLike()); // 토글 확인
        verify(heartRepository).save(heart);
    }

    @Test
    @DisplayName("좋아요 토글 실패 - 게시글 없음")
    void convertLikeStatus_postNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> memberService.convertLikeStatus(1L, 1L));

        verify(postRepository).findById(1L);
        verifyNoInteractions(memberRepository, heartRepository);
    }

    @Test
    @DisplayName("좋아요 토글 실패 - 멤버 없음")
    void convertLikeStatus_memberNotFound() {
        Post post = new Post("content", "writer", 0L);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> memberService.convertLikeStatus(1L, 1L));

        verify(postRepository).findById(1L);
        verify(memberRepository).findById(1L);
        verifyNoInteractions(heartRepository);
    }
}
