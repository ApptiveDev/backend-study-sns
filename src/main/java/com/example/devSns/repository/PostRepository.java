package com.example.devSns.repository;

import com.example.devSns.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class PostRepository {
    private final JdbcClient jdbcClient;
    private final SimpleJdbcInsert insert;

    @Autowired
    public PostRepository(JdbcClient jdbcClient, DataSource dataSource) {
        this.jdbcClient = jdbcClient;
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("posts")
                .usingColumns("content", "user_name")
                .usingGeneratedKeyColumns("id");

    }

    public Post save(Post post) {
        Map<String, Object> params = Map.of(
                "content", post.getContent(),
                "user_name", post.getUserName()
        );
        Number key = insert.executeAndReturnKey(params);
        post.setId(key.longValue());
        return post;
    }

    public Optional<Post> findById(Long id) {
        return jdbcClient.sql("SELECT * FROM posts WHERE id = :id")
                .param("id", id)
                .query(Post.class)
                .optional();
    }

    public Optional<Post> findByUsername(String userName) {
        return jdbcClient.sql("SELECT * FROM posts WHERE user_name = :userName")
                .param("userName", userName)
                .query(Post.class)
                .optional();
    }

    public List<Post> findByBeforeCreatedAt(LocalDateTime createdAt) {
        // 한번당 쿼리 개수를 최대 15개로 제한
        Integer limit = 15;
        return jdbcClient.sql("""
                    SELECT *
                    FROM posts
                    WHERE CREATED_AT < :createdAt
                    ORDER BY created_at DESC, id DESC
                    LIMIT :limit
                """)
                .param("createdAt", createdAt)
                .param("limit", limit)
                .query(Post.class)
                .list();
    }

    public List<Post> findAll() {
        return jdbcClient.sql("SELECT * FROM posts")
                .query(Post.class)
                .list();
    }

    public int updateById(Post post, Long id) {
        return jdbcClient.sql("UPDATE posts SET LIKE_COUNT = ?, CONTENT = ?, USER_NAME = ? WHERE id = ?")
                .params(post.getLikeCount(), post.getContent(), post.getUserName(), id)
                .update();
    }

    public int incrementLikeById(Long id) {
        return jdbcClient.sql("UPDATE posts SET LIKE_COUNT = LIKE_COUNT + 1 WHERE id = ?")
            .params(id)
            .update();
    }

    public int updateContentByIdAndUpdatedAt(String content, Long id, LocalDateTime updatedAt) {
        return jdbcClient.sql("UPDATE posts SET CONTENT = ? WHERE id = ? AND UPDATED_AT = ?")
            .params(content, id, updatedAt)
            .update();
    }



    public int deleteById(Long id) {
        return jdbcClient.sql("DELETE FROM POSTS WHERE ID=:id")
                .param("id", id)
                .update();
    }

}
