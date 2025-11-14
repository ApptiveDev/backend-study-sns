package com.example.devSns;

import com.example.devSns.entity.Member;
import com.example.devSns.entity.Post;
import com.example.devSns.repository.LikeRepository;
import com.example.devSns.repository.MemberRepository;
import com.example.devSns.repository.PostRepository;
import com.example.devSns.service.LikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class LikeServiceTest {
    @Autowired
    private LikeService likeService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;

    private Member member;
    private Post post;

    @BeforeEach
    public void setup() {
        member = Member.create("user1","user1@test.com","1234");
        memberRepository.save(member);

        post = Post.create("테스트 게시글",member);
        postRepository.save(post);
    }

    @Test
    @DisplayName("멤버 게시글에 좋아요 가능")
    void memberCanLikePost(){
        likeService.toggleLike(member.getId(),post.getId());
        boolean exists = likeRepository.existsByMemberAndPost(member,post);
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("좋아요 두번 누르면 취소 가능")
    void likeIsToggled(){
        likeService.toggleLike(member.getId(),post.getId());
        assertThat(likeRepository.countByPost(post)).isEqualTo(1);

        likeService.toggleLike(member.getId(),post.getId());
        assertThat(likeRepository.countByPost(post)).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글의 좋아요 수 조회 가능")
    void canCountLikes(){
        likeService.toggleLike(member.getId(),post.getId());
        long count = likeService.getLikeCount(post.getId());
        assertThat(count).isEqualTo(1);
    }
}
