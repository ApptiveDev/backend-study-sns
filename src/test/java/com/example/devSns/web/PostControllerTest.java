package com.example.devSns.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;

import jakarta.transaction.Transactional;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // 각 테스트 후 롤백
class PostControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper om;

    @Test
    void post_CRUD_flow() throws Exception {
        // CREATE
        MvcResult createRes = mockMvc.perform(
                        post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(Map.of("title","첫 글","content","내용입니다")))
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("첫 글"))
                .andReturn();

        long postId = om.readTree(createRes.getResponse().getContentAsString()).get("id").asLong();

        // LIST
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists());

        // DETAIL
        mockMvc.perform(get("/posts/{id}", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("첫 글"));

        // UPDATE
        mockMvc.perform(
                        put("/posts/{id}", postId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(Map.of("title","수정 제목","content","수정 내용")))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정 제목"))
                .andExpect(jsonPath("$.content").value("수정 내용"));

        // DELETE
        mockMvc.perform(delete("/posts/{id}", postId))
                .andExpect(status().isNoContent());

        // 404 확인
        mockMvc.perform(get("/posts/{id}", postId))
                .andExpect(status().isNotFound());
    }

    @Test
    void post_create_validation_fail_when_title_blank() throws Exception {
        mockMvc.perform(
                post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("title","", "content","x")))
        ).andExpect(status().isBadRequest());
    }
}
