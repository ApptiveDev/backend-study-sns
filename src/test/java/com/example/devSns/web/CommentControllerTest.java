package com.example.devSns.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;

import jakarta.transaction.Transactional;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CommentControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper om;

    long postId;

    @BeforeEach
    void setUp() throws Exception {
        // 테스트용 게시글 하나 생성
        MvcResult res = mockMvc.perform(
                        post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(Map.of("title","댓글 테스트 글","content","본문")))
                ).andExpect(status().isCreated())
                .andReturn();

        postId = om.readTree(res.getResponse().getContentAsString()).get("id").asLong();
    }

    @Test
    void comment_CRUD_flow() throws Exception {
        // CREATE
        MvcResult create = mockMvc.perform(
                        post("/posts/{postId}/comments", postId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(Map.of("content","첫 댓글")))
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.content").value("첫 댓글"))
                .andReturn();

        long commentId = om.readTree(create.getResponse().getContentAsString()).get("id").asLong();

        // LIST
        mockMvc.perform(get("/posts/{postId}/comments", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(commentId));

        // DETAIL
        mockMvc.perform(get("/posts/{postId}/comments/{commentId}", postId, commentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("첫 댓글"));

        // UPDATE
        mockMvc.perform(
                        put("/posts/{postId}/comments/{commentId}", postId, commentId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(Map.of("content","수정된 댓글")))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("수정된 댓글"));

        // DELETE
        mockMvc.perform(delete("/posts/{postId}/comments/{commentId}", postId, commentId))
                .andExpect(status().isNoContent());

        // 404 확인
        mockMvc.perform(get("/posts/{postId}/comments/{commentId}", postId, commentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void comment_create_validation_fail_when_blank() throws Exception {
        mockMvc.perform(
                post("/posts/{postId}/comments", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("content","")))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void comment_404_when_post_not_found() throws Exception {
        mockMvc.perform(
                post("/posts/{postId}/comments", 999_999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("content","x")))
        ).andExpect(status().isNotFound());
    }
}
