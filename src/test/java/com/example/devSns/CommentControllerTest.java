package com.example.devSns;

import com.example.devSns.entity.Comment;
import com.example.devSns.entity.Member;
import com.example.devSns.entity.Post;
import com.example.devSns.repository.CommentRepository;
import com.example.devSns.repository.MemberRepository;
import com.example.devSns.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Post savedPost;
    private Member savedMember;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        memberRepository.deleteAll();

        // 1) 멤버 생성
        savedMember = memberRepository.save(
                Member.create("writer", "writer@test.com", "1234")
        );

        // 2) 게시글 생성
        savedPost = postRepository.save(
                Post.create("댓글 테스트용 게시글", savedMember)
        );
    }

    @Test
    void 댓글_작성_성공() throws Exception {

        String json = """
        {
          "memberId": %d,
          "content": "좋은 글이네요!"
        }
        """.formatted(savedMember.getId());

        mockMvc.perform(post("/posts/" + savedPost.getId() + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("좋은 글이네요!"))
                .andExpect(jsonPath("$.memberId").value(savedMember.getId()))
                .andExpect(jsonPath("$.postId").value(savedPost.getId()));
    }

    @Test
    void 댓글_조회_성공() throws Exception {

        Comment comment = commentRepository.save(
                Comment.create("조회용 댓글", savedMember, savedPost)
        );

        mockMvc.perform(get("/posts/" + savedPost.getId() + "/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("조회용 댓글"))
                .andExpect(jsonPath("$[0].memberId").value(savedMember.getId()));
    }

    @Test
    void 댓글_수정_성공() throws Exception {

        Comment comment = commentRepository.save(
                Comment.create("원본 댓글", savedMember, savedPost)
        );

        String updateJson = """
        {
          "content": "수정된 댓글입니다."
        }
        """;

        mockMvc.perform(patch("/comments/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("수정된 댓글입니다."));
    }

    @Test
    void 댓글_삭제_성공() throws Exception {

        Comment comment = commentRepository.save(
                Comment.create("삭제용 댓글", savedMember, savedPost)
        );

        mockMvc.perform(delete("/comments/" + comment.getId()))
                .andExpect(status().isOk());

        assertThat(commentRepository.existsById(comment.getId())).isFalse();
    }
}
