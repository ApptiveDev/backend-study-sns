package com.example.devSns;

import com.example.devSns.dto.GenericDataDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 전체 빈을 띄우고, MockMvc로 HTTP 레벨부터 컨트롤러/서비스/리포지토리/H2까지 검증한다.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.DisplayName.class)
@Transactional
class ApiIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    private String asJson(Object o) throws Exception {
        return om.writeValueAsString(o);
    }
    private <T> T fromJson(String json, Class<T> clz) throws Exception {
        return om.readValue(json, clz);
    }
    private Map<String, Object> toMap(String json) throws Exception {
        return om.readValue(json, new TypeReference<>() {});
    }

    @Nested
    @DisplayName("1. Posts API")
    class PostsApi {

        @Test
        @DisplayName("1-1. POST /posts -> 201 Created + Location + {id}")
        void createPost() throws Exception {
            var body = Map.of("content", "hello world", "user_name", "alice");

            MvcResult result = mvc.perform(
                            post("/posts")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJson(body))
                    ).andExpect(status().isCreated())
                    .andExpect(header().exists("Location"))
                    .andExpect(jsonPath("$.data").exists())
                    .andReturn();

            var resp = toMap(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
            assertThat(resp.get("data")).isInstanceOf(Number.class);
        }

        @Test
        @DisplayName("1-2. GET /posts/{id} -> 단건 조회 + 댓글 리스트(초기 0)")
        void getPost() throws Exception {
            // 먼저 하나 생성
            var created = mvc.perform(
                    post("/posts").contentType(MediaType.APPLICATION_JSON)
                            .content(asJson(Map.of("content", "post content", "user_name", "bob")))
            ).andReturn();
            Long id = ((Number) toMap(created.getResponse().getContentAsString()).get("data")).longValue();

            mvc.perform(get("/posts/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id))
                    .andExpect(jsonPath("$.content").value("post content"))
                    .andExpect(jsonPath("$.user_name").value("bob"))
                    .andExpect(jsonPath("$.like_count").value(0))
                    .andExpect(jsonPath("$.comments").isArray());
        }

        @Test
        @DisplayName("1-3. PATCH /posts/{id}/likes -> 좋아요 +1")
        void likePost() throws Exception {
            // 생성
            Long id = ((Number) toMap(
                    mvc.perform(post("/posts")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJson(Map.of("content","like me","user_name","cathy"))))
                            .andReturn().getResponse().getContentAsString()
            ).get("data")).longValue();

            // 좋아요
            mvc.perform(post("/posts/{id}/likes", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id))
                    .andExpect(jsonPath("$.like_count").value(1));
        }

        @Test
        @DisplayName("1-4. PATCH /posts/{id}/contents -> 본문 수정")
        void updatePostContent() throws Exception {
            Long id = ((Number) toMap(
                    mvc.perform(post("/posts")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJson(Map.of("content","old","user_name","danny"))))
                            .andReturn().getResponse().getContentAsString()
            ).get("data")).longValue();

            mvc.perform(post("/posts/{id}/contents", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJson(new GenericDataDto<>("new content"))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").value("new content"));
        }

        @Test
        @DisplayName("1-5. GET /posts (페이지네이션) -> nextQueryCriteria 검증")
        void listPostsPaginated() throws Exception {
            // 여러 개 생성
            for (int i = 0; i < 3; i++) {
                mvc.perform(post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJson(Map.of("content","c"+i,"user_name","u"))))
                        .andExpect(status().isCreated());
            }

            // 첫 페이지 (criteria=null) -> body에 { "data": null } 전달
            var firstPage = mvc.perform(get("/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJson(new GenericDataDto<LocalDateTime>(null))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.nextQueryCriteria").exists())
                    .andReturn();

            var firstPageMap = toMap(firstPage.getResponse().getContentAsString());
            assertThat(firstPageMap.get("data")).isInstanceOfAny(java.util.List.class);
            assertThat(firstPageMap.get("nextQueryCriteria")).isNotNull();
        }

        @Test
        @DisplayName("1-6. DELETE /posts/{id} -> 204, 없는 id는 404")
        void deletePost() throws Exception {
            Long id = ((Number) toMap(
                    mvc.perform(post("/posts")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJson(Map.of("content","to delete","user_name","eve"))))
                            .andReturn().getResponse().getContentAsString()
            ).get("data")).longValue();

            mvc.perform(delete("/posts/{id}", id)).andExpect(status().isNoContent());
            mvc.perform(delete("/posts/{id}", id)).andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("2. Comments API")
    class CommentsApi {

        private Long newPostId() throws Exception {
            return ((Number) toMap(
                    mvc.perform(post("/posts")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJson(Map.of("content","for comments","user_name","post-owner"))))
                            .andReturn().getResponse().getContentAsString()
            ).get("data")).longValue();
        }

        @Test
        @DisplayName("2-1. POST /comments -> 201 + {id}")
        void createComment() throws Exception {
            Long postId = newPostId();
            var body = Map.of("post_id", postId, "content", "hi", "user_name", "alice");

            mvc.perform(post("/comments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJson(body)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data").exists())
                    .andExpect(header().exists("Location"));
        }

        @Test
        @DisplayName("2-2. GET /comments/{id} -> 단건 조회")
        void getComment() throws Exception {
            Long postId = newPostId();
            Long id = ((Number) toMap(
                    mvc.perform(post("/comments")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJson(Map.of("post_id", postId, "content", "cmt", "user_name", "bob"))))
                            .andReturn().getResponse().getContentAsString()
            ).get("data")).longValue();

            mvc.perform(get("/comments/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id))
                    .andExpect(jsonPath("$.post_id").value(postId))
                    .andExpect(jsonPath("$.content").value("cmt"))
                    .andExpect(jsonPath("$.like_count").value(0));
        }

        @Test
        @DisplayName("2-3. PATCH /comments/{id}/likes -> 좋아요 +1")
        void likeComment() throws Exception {
            Long postId = newPostId();
            Long id = ((Number) toMap(
                    mvc.perform(post("/comments")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJson(Map.of("post_id", postId, "content", "like me", "user_name", "cathy"))))
                            .andReturn().getResponse().getContentAsString()
            ).get("data")).longValue();

            mvc.perform(post("/comments/{id}/likes", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id))
                    .andExpect(jsonPath("$.like_count").value(1));
        }

        @Test
        @DisplayName("2-4. PATCH /comments/{id}/contents -> 본문 수정")
        void updateCommentContent() throws Exception {
            Long postId = newPostId();
            Long id = ((Number) toMap(
                    mvc.perform(post("/comments")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJson(Map.of("post_id", postId, "content", "old", "user_name", "danny"))))
                            .andReturn().getResponse().getContentAsString()
            ).get("data")).longValue();

            mvc.perform(post("/comments/{id}/contents", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJson(new GenericDataDto<>("new"))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").value("new"));
        }

        @Test
        @DisplayName("2-5. GET /comments/postGroup/{postId} (페이지네이션)")
        void listCommentsPaginated() throws Exception {
            Long postId = newPostId();
            // 여러 개 생성
            for (int i = 0; i < 3; i++) {
                mvc.perform(post("/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJson(Map.of("post_id", postId, "content", "c"+i, "user_name", "u"))))
                        .andExpect(status().isCreated());
            }

            mvc.perform(get("/comments/postGroup/{postId}", postId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJson(new GenericDataDto<LocalDateTime>(null))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.nextQueryCriteria").exists());
        }

        @Test
        @DisplayName("2-6. DELETE /comments/{id} -> 204, 없는 id는 404")
        void deleteComment() throws Exception {
            Long postId = newPostId();
            Long id = ((Number) toMap(
                    mvc.perform(post("/comments")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJson(Map.of("post_id", postId, "content", "bye", "user_name", "eve"))))
                            .andReturn().getResponse().getContentAsString()
            ).get("data")).longValue();

            mvc.perform(delete("/comments/{id}", id)).andExpect(status().isNoContent());
            mvc.perform(delete("/comments/{id}", id)).andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("3. 예외/검증")
    class ErrorCases {
        @Test
        @DisplayName("3-1. 유효성 오류(빈 content) -> 400 Bad Request")
        void validationError() throws Exception {
            mvc.perform(post("/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJson(Map.of("content","", "user_name","x"))))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("3-2. 존재하지 않는 post/comment 조회 -> 404 Not Found")
        void notFound() throws Exception {
            mvc.perform(get("/posts/{id}", 999999)).andExpect(status().isNotFound());
            mvc.perform(get("/comments/{id}", 999999)).andExpect(status().isNotFound());
        }
    }
}
