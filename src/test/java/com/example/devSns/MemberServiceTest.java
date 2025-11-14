package com.example.devSns;

import com.example.devSns.dto.CommentResponse;
import com.example.devSns.dto.PostResponse;
import com.example.devSns.entity.*;
import com.example.devSns.repository.CommentRepository;
import com.example.devSns.repository.MemberRepository;
import com.example.devSns.repository.PostRepository;
import com.example.devSns.repository.LikeRepository;
import com.example.devSns.service.MemberService;
import com.example.devSns.service.LikeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Autowired PostRepository postRepository;
    @Autowired CommentRepository commentRepository;
    @Autowired LikeRepository likeRepository;

    @Autowired LikeService likeService;

    @Test
    @DisplayName("회원 가입 성공")
    void 회원가입_성공() {
        Member member = Member.create("강지원", "test@test.com", "1234");

        memberService.join(member);

        Member found = memberRepository.findByUsername("강지원")
                .orElseThrow();

        assertThat(found.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("중복 아이디 가입 예외")
    void 중복회원_예외() {
        Member m1 = Member.create("강지원", "a@test.com", "1111");
        Member m2 = Member.create("강지원", "b@test.com", "2222");

        memberService.join(m1);

        assertThatThrownBy(() -> memberService.join(m2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Already exists member");
    }

    @Test
    @DisplayName("이름으로 회원 검색 성공")
    void 회원검색_성공() {
        Member m1 = Member.create("강지원", "a@test.com", "1111");
        Member m2 = Member.create("먼지", "b@test.com", "2222");

        memberRepository.saveAll(List.of(m1, m2));

        List<Member> result = memberService.searchMembers("강");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("강지원");
    }

    @Test
    @DisplayName("회원이 작성한 게시글 조회")
    void 회원게시글조회() {

        Member member = memberRepository.save(Member.create("강지원", "t@t.com", "1234"));

        Post p1 = postRepository.save(Post.create("post1", member));
        Post p2 = postRepository.save(Post.create("post2", member));

        List<PostResponse> posts = memberService.getPostsByMember(member.getId());

        assertThat(posts).hasSize(2);
        assertThat(posts.get(0).getContent()).isEqualTo("post1");
        assertThat(posts.get(1).getContent()).isEqualTo("post2");
    }

    @Test
    @DisplayName("회원이 작성한 댓글 조회")
    void 회원댓글조회() {

        Member member = memberRepository.save(Member.create("tester", "t@t.com", "1111"));
        Post post = postRepository.save(Post.create("게시글", member));

        Comment c1 = commentRepository.save(Comment.builder()
                .content("댓글1")
                .username("tester")
                .post(post)
                .member(member)
                .build());
        c1.assignMember(member);
        commentRepository.save(c1);

        Comment c2 = commentRepository.save(Comment.builder()
                .content("댓글2")
                .username("tester")
                .post(post)
                .member(member)
                .build());
        c1.assignMember(member);
        commentRepository.save(c2);

        List<CommentResponse> comments = memberService.getCommentsByMember(member.getId());

        assertThat(comments).hasSize(2);
        assertThat(comments.get(0).content()).isIn("댓글1", "댓글2");
    }

    @Test
    @DisplayName("회원이 좋아요한 게시글 조회")
    void 회원좋아요조회() {

        Member member = memberRepository.save(Member.create("user", "u@u.com", "1234"));
        Post post1 = postRepository.save(Post.create("좋아요 게시글1", member));
        Post post2 = postRepository.save(Post.create("좋아요 게시글2", member));

        likeRepository.save(Like.create(member, post1));
        likeRepository.save(Like.create(member, post2));

        List<PostResponse> likedPosts = memberService.getLikedPosts(member.getId());

        assertThat(likedPosts).hasSize(2);
        assertThat(likedPosts.get(0).getContent()).isIn("좋아요 게시글1", "좋아요 게시글2");
    }

}
