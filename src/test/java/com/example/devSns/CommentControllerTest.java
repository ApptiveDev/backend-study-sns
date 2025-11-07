package com.example.devSns;

import com.example.devSns.entity.Comment;
import com.example.devSns.entity.Post;
import com.example.devSns.repository.CommentRepository;
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
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    private Post savedPost;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        postRepository.deleteAll();

        savedPost = postRepository.save(Post.builder()
                .username("dardar")
                .content("댓글 테스트용 게시글")
                .build());
    }

    @Test
    void 댓글_작성_성공() throws Exception {
        String json = """
            {
              "username": "tester",
              "content": "좋은 글이네요!"
            }
        """;

        mockMvc.perform(post("/post/" + savedPost.getId() + "/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("tester"))
                .andExpect(jsonPath("$.content").value("좋은 글이네요!"));
    }

    @Test
    void 댓글_조회_성공() throws Exception {
        Comment comment = commentRepository.save(Comment.builder()
                .username("tester")
                .content("조회용 댓글")
                .post(savedPost)
                .build());

        mockMvc.perform(get("/post/" + savedPost.getId() + "/comment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("조회용 댓글"))
                .andExpect(jsonPath("$[0].username").value("tester"));
    }

    @Test
    void 댓글_수정_성공() throws Exception {
        Comment comment = commentRepository.save(Comment.builder()
                .username("tester")
                .content("원본 댓글")
                .post(savedPost)
                .build());

        String updateJson = """
            {
              "content": "수정된 댓글입니다."
            }
        """;

        mockMvc.perform(patch("/post/" + savedPost.getId() + "/comment/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("수정된 댓글입니다."));
    }

    @Test
    void 댓글_삭제_성공() throws Exception {
        Comment comment = commentRepository.save(Comment.builder()
                .username("tester")
                .content("삭제용 댓글")
                .post(savedPost)
                .build());

        mockMvc.perform(delete("/post/" + savedPost.getId() + "/comment/" + comment.getId()))
                .andExpect(status().isOk());
    }
}
