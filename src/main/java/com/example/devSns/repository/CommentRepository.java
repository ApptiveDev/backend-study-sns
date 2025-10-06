package com.example.devSns.repository;

import com.example.devSns.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class CommentRepository {
    private final JdbcClient jdbcClient;
//    private final KeyHolder keyHolder;
    private final SimpleJdbcInsert insert;
    @Autowired
    public CommentRepository(JdbcClient jdbcClient, DataSource dataSource) {
        this.jdbcClient = jdbcClient;
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("comments")
                .usingColumns("content", "user_name", "post_id")
                .usingGeneratedKeyColumns("id");
    }

    public Comment save(Comment comment) {
        Map<String, Object> params = Map.of(
                "content", comment.getContent(),
                "user_name", comment.getUserName(),
                "post_id", comment.getPostId()
        );

//        comment.setId(keyHolder.getKey().longValue());
        Number key = insert.executeAndReturnKey(params);
        comment.setId(key.longValue());
        return comment;
    }

    public Optional<Comment> findById(Long id) {
        return jdbcClient.sql("SELECT * FROM COMMENTS WHERE id = :id")
                .param("id", id)
                .query(Comment.class)
                .optional();
    }

    public Optional<Comment> findByUsername(String userName) {
        return jdbcClient.sql("SELECT * FROM COMMENTS WHERE user_name = :userName")
                .param("userName", userName)
                .query(Comment.class)
                .optional();
    }

    public List<Comment> findByBeforeCreatedAtAndPostId(LocalDateTime createdAt, Long postId) {
        // 한번당 쿼리 개수를 최대 15개로 제한
        Integer limit = 15;
        return jdbcClient.sql("""
                    SELECT *
                    FROM COMMENTS
                    WHERE CREATED_AT < :createdAt and POST_ID = :postId
                    ORDER BY created_at DESC, id DESC
                    LIMIT :limit
                """)
                .param("createdAt", createdAt)
                .param("limit", limit)
                .param("postId", postId)
                .query(Comment.class)
                .list();
    }

    public List<Comment> findAll() {
        return jdbcClient.sql("SELECT * FROM COMMENTS")
                .query(Comment.class)
                .list();
    }

    public int updateById(Comment comment, Long id) {
        return jdbcClient.sql("UPDATE COMMENTS SET LIKE_COUNT = ?, CONTENT = ?, USER_NAME = ? WHERE id = ?")
                .params(comment.getLikeCount(), comment.getContent(), comment.getUserName(), id)
                .update();
    }

    public int deleteById(Long id) {
        return jdbcClient.sql("DELETE FROM COMMENTS WHERE ID=:id")
                .param("id", id)
                .update();
    }

    public Map<Long, List<Comment>> findAndGroupByPostIds(List<Long> postIds) {
        List<Comment> comments = jdbcClient.sql("""
            SELECT x.*
            FROM (
                SELECT c.*,
                    ROW_NUMBER() over (
                    partition by c.POST_ID
                    ORDER BY c.CREATED_AT DESC, c.id DESC 
                ) AS rn
                FROM COMMENTS c
                WHERE c.POST_ID IN (:postIds)
            ) as x
            WHERE x.rn <= 5
            ORDER BY x.POST_ID, x.CREATED_AT DESC, x.id DESC 
        """).param("postIds", postIds)
                .query(Comment.class)
                .list();

        return comments.stream().collect(
                Collectors.groupingBy(
                        Comment::getPostId,
                        LinkedHashMap::new,
                        Collectors.toList()
                )
        );
    }

//    private RowMapper<Post> postRowMapper() {
//        return (rs, rowNum) -> {
//            Post post = new Post();
//            post.setId(rs.getLong("id"));
//            post.setUserName(rs.getString("user_name"));
//            post.setContent(rs.getString("content"));
//            post.setLikeCount(rs.getLong("like_count"));
//            post.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
//            post.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
//            return post;
//        };
//    }
}
