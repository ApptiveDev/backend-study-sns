package com.example.devSns;

import com.example.devSns.entity.Post;
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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void cleanDB() {
        postRepository.deleteAll();
    }

    @Test
    void 게시글_생성_성공() throws Exception {
        String json = """
            {
              "username": "dardar",
              "content": "테스트 게시글입니다."
            }
        """;

        mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("dardar"))
                .andExpect(jsonPath("$.content").value("테스트 게시글입니다."));
    }

    @Test
    void 게시글_조회_성공() throws Exception {
        Post post = Post.builder()
                .username("tester")
                .content("조회용 게시글")
                .build();
        postRepository.save(post);

        mockMvc.perform(get("/post/" + post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("tester"))
                .andExpect(jsonPath("$.content").value("조회용 게시글"));
    }

    @Test
    void 게시글_수정_성공() throws Exception {
        Post post = postRepository.save(Post.builder()
                .username("tester")
                .content("원본 내용")
                .build());

        String updateJson = """
            {
              "content": "수정된 내용"
            }
        """;

        mockMvc.perform(put("/post/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("수정된 내용"));
    }

    @Test
    void 게시글_삭제_성공() throws Exception {
        Post post = postRepository.save(Post.builder()
                .username("tester")
                .content("삭제 대상 게시글")
                .build());

        mockMvc.perform(delete("/post/" + post.getId()))
                .andExpect(status().isOk());
    }
}
