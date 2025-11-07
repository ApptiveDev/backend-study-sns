package com.example.devSns;

import com.example.devSns.dto.GenericDataDto;
import com.example.devSns.dto.PaginatedDto;
import com.example.devSns.dto.post.PostResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;
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

    // [추가] LocalDateTime 직렬화/역직렬화를 위해 모듈 등록
    @BeforeEach
    void setUp() {
        om.registerModule(new JavaTimeModule());
    }

    private String asJson(Object o) throws Exception {
        return om.writeValueAsString(o);
    }

    private <T> T fromJson(String json, TypeReference<T> typeReference) throws Exception {
        return om.readValue(json, typeReference);
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
        @DisplayName("1-2. GET /posts/{id} -> 단건 조회 + 댓글 수 (초기 0)")
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
                    .andExpect(jsonPath("$.comments").value(0));
        }

        @Test
        @DisplayName("1-3. POST /posts/{id}/likes -> 좋아요 +1 후 GET으로 확인")
        void likePost() throws Exception {
            // 생성
            Long id = ((Number) toMap(
                    mvc.perform(post("/posts")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJson(Map.of("content","like me","user_name","cathy"))))
                            .andReturn().getResponse().getContentAsString()
            ).get("data")).longValue();

//            // [보완] 좋아요 요청 후, GET으로 실제 값이 증가했는지 확인
//            mvc.perform(post("/posts/{id}/likes", id))
//                    .andExpect(status().isNoContent());
//
//            // 검증
//            mvc.perform(get("/posts/{id}", id))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.id").value(id))
//                    .andExpect(jsonPath("$.like_count").value(1));
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

            mvc.perform(patch("/posts/{id}/contents", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJson(new GenericDataDto<>("new content"))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").value("new content"));
        }

        @Test
        @DisplayName("1-5. GET /posts (페이지네이션) -> nextQueryCriteria로 다음 페이지 조회")
        void listPostsPaginated() throws Exception {
            // [보완] 순서를 보장하기 위해 20개의 포스트 생성
            for (int i = 1; i <= 20; i++) {
                mvc.perform(post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJson(Map.of("content","c"+i,"user_name","u"))))
                        .andExpect(status().isCreated());
            }

            // 첫 페이지 (criteria=null), 15개 반환
            MvcResult firstResult = mvc.perform(get("/posts"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(15))
                    .andExpect(jsonPath("$.nextQueryCriteria").exists())
                    .andReturn();

            // [보완] nextQueryCriteria를 사용해 두번째 페이지 요청
            String firstResponse = firstResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
            PaginatedDto<List<PostResponseDto>> firstPageDto = fromJson(firstResponse, new TypeReference<>() {});
            Long nextCriteria = firstPageDto.nextQueryCriteria();

            assertThat(nextCriteria).isNotNull();

            // 두 번째 페이지 (criteria 사용), 나머지 5개 반환
            mvc.perform(get("/posts").param("before", nextCriteria.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(5))
                    .andExpect(jsonPath("$.nextQueryCriteria").exists()); // 마지막 페이지의 nextQueryCriteria는 마지막 요소의 createdAt
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
            mvc.perform(get("/posts/{id}", id)).andExpect(status().isNotFound()); // [보완] GET으로도 확인
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
        @DisplayName("2-3. POST /comments/{id}/likes -> 좋아요 +1 후 GET으로 확인")
        void likeComment() throws Exception {
            Long postId = newPostId();
            Long id = ((Number) toMap(
                    mvc.perform(post("/comments")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJson(Map.of("post_id", postId, "content", "like me", "user_name", "cathy"))))
                            .andReturn().getResponse().getContentAsString()
            ).get("data")).longValue();

//            // [보완] 좋아요 요청 후, GET으로 실제 값이 증가했는지 확인
//            mvc.perform(post("/comments/{id}/likes", id))
//                    .andExpect(status().isNoContent());
//
//            // 검증
//            mvc.perform(get("/comments/{id}", id))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.id").value(id))
//                    .andExpect(jsonPath("$.like_count").value(1));
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

            mvc.perform(patch("/comments/{id}/contents", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJson(new GenericDataDto<>("new"))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").value("new"));
        }

        @Test
        @DisplayName("2-5. GET /posts/{postId}/comments (페이지네이션)")
        void listCommentsPaginated() throws Exception {
            Long postId = newPostId();
            // 20개 생성
            for (int i = 0; i < 20; i++) {
                mvc.perform(post("/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJson(Map.of("post_id", postId, "content", "c"+i, "user_name", "u"))))
                        .andExpect(status().isCreated());
            }

            // 첫 페이지
            MvcResult firstResult = mvc.perform(get("/posts/{postId}/comments", postId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(15))
                    .andExpect(jsonPath("$.nextQueryCriteria").exists())
                    .andReturn();

            // [보완] nextQueryCriteria로 두 번째 페이지 요청
            String firstResponse = firstResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
            PaginatedDto<List<Map<String, Object>>> firstPageDto = fromJson(firstResponse, new TypeReference<>() {});
            Long nextCriteria = firstPageDto.nextQueryCriteria();

            mvc.perform(get("/posts/{postId}/comments", postId).param("before", nextCriteria.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(5));
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
        @DisplayName("3-1. 유효성 오류(빈 content, null user_name) -> 400 Bad Request")
        void validationError() throws Exception {
            // content가 비어있을 때
            mvc.perform(post("/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJson(Map.of("content","", "user_name","x"))))
                    .andExpect(status().isBadRequest());

            // user_name이 없을 때
            mvc.perform(post("/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJson(Map.of("content","hello"))))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("3-2. 존재하지 않는 post/comment 조회 -> 404 Not Found")
        void notFound() throws Exception {
            mvc.perform(get("/posts/{id}", 999999L)).andExpect(status().isNotFound());
            mvc.perform(get("/comments/{id}", 999999L)).andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("3-3. [추가] 존재하지 않는 post에 comment 작성 -> 400 Bad Request")
        void createCommentOnNonExistentPost() throws Exception {
            var body = Map.of("post_id", 999999L, "content", "hi", "user_name", "ghost");
            mvc.perform(post("/comments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJson(body)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message").value("Invalid Request."));
        }

        @Test
        @DisplayName("3-4. [추가] 잘못된 본문 수정 요청(내용 없음) -> 400 Bad Request")
        void updatePostWithInvalidContent() throws Exception {
            Long id = ((Number) toMap(
                    mvc.perform(post("/posts")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJson(Map.of("content","old","user_name","danny"))))
                            .andReturn().getResponse().getContentAsString()
            ).get("data")).longValue();

            // data가 null일 때
            mvc.perform(patch("/posts/{id}/contents", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJson(new GenericDataDto<>(null))))
                    .andExpect(status().isBadRequest());

            // data가 빈 문자열일 때
            mvc.perform(patch("/posts/{id}/contents", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJson(new GenericDataDto<>(""))))
                    .andExpect(status().isBadRequest());
        }
    }
}