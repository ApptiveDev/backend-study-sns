package com.example.devSns;

import com.example.devSns.entity.Member;
import com.example.devSns.entity.Post;
import com.example.devSns.repository.MemberRepository;
import com.example.devSns.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired PostRepository postRepository;
    @Autowired MemberRepository memberRepository;

    @BeforeEach
    void cleanDB() {
        postRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    void 게시글_생성_성공() throws Exception {

        Member member = memberRepository.save(
                Member.create("강지원", "test@test.com", "1234")
        );

        String json = """
        {
          "memberId": %d,
          "content": "테스트 게시글입니다."
        }
        """.formatted(member.getId());

        mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("테스트 게시글입니다."))
                .andExpect(jsonPath("$.memberId").value(member.getId()));
    }

    @Test
    void 게시글_조회_성공() throws Exception {

        Member member = memberRepository.save(
                Member.create("tester", "tester@test.com", "1234")
        );

        Post post = postRepository.save(
                Post.create("조회용 게시글", member)
        );

        mockMvc.perform(get("/post/" + post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("조회용 게시글"))
                .andExpect(jsonPath("$.memberId").value(member.getId()));
    }

    @Test
    void 게시글_수정_성공() throws Exception {

        Member member = memberRepository.save(
                Member.create("tester", "tester@test.com", "1234")
        );

        Post post = postRepository.save(
                Post.create("원본 내용", member)
        );

        String updateJson = """
            {
              "content": "수정된 내용"
            }
        """;

        mockMvc.perform(patch("/post/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("수정된 내용"));
    }

    @Test
    void 게시글_삭제_성공() throws Exception {

        Member member = memberRepository.save(
                Member.create("tester", "tester@test.com", "1234")
        );

        Post post = postRepository.save(
                Post.create("삭제 대상 게시글", member)
        );

        mockMvc.perform(delete("/post/" + post.getId()))
                .andExpect(status().isOk());
    }
}
