package com.example.devSns.controller;

import com.example.devSns.domain.Comment;
import com.example.devSns.domain.Post;
import com.example.devSns.repository.CommentRepository;
import com.example.devSns.repository.PostRepository;
import jakarta.persistence.EntityManager; // 1. 임포트 확인
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // 테스트 후 DB 롤백 (데이터 깔끔하게)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EntityManager em; // 2. EntityManager 주입

    private Post savedPost;
    private Comment parentComment;
    private Comment childComment;

    @BeforeEach // 각 테스트 실행 전에 DB 세팅
    void setUp() {
        // 1. Given (준비)
        savedPost = postRepository.save(new Post("테스트 게시글", "내용"));

        parentComment = commentRepository.save(
                new Comment(savedPost, null, "1번 부모 댓글")
        );

        childComment = commentRepository.save(
                new Comment(savedPost, parentComment, "1-1번 자식 댓글(대댓글)")
        );

        // 3. DB 강제 반영 및 1차 캐시 초기화 (★수정된 부분★)
        em.flush();
        em.clear();
    }

    @DisplayName("특정 게시글의 댓글/대댓글 목록을 올바르게 조회한다.")
    @Test
    void getCommentsByPost() throws Exception {
        // 2. When (실행)
        // savedPost의 ID를 동적으로 가져와서 URL을 만듭니다.
        mockMvc.perform(get("/api/posts/" + savedPost.getId() + "/comments"))

                // 3. Then (검증)
                .andExpect(status().isOk())
                // $는 JSON 전체 응답, $[0]는 첫번째 댓글
                .andExpect(jsonPath("$.length()").value(1)) // 최상위 댓글은 1개
                .andExpect(jsonPath("$[0].id").value(parentComment.getId()))
                .andExpect(jsonPath("$[0].content").value("1번 부모 댓글"))
                // 이게 진짜배기: 자식 댓글(children) 검증
                .andExpect(jsonPath("$[0].children.length()").value(1)) // 1번 댓글의 자식은 1개
                .andExpect(jsonPath("$[0].children[0].id").value(childComment.getId()))
                .andExpect(jsonPath("$[0].children[0].content").value("1-1번 자식 댓글(대댓글)"));
    }
}