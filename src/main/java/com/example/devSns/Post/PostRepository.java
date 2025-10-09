package com.example.devSns.Post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PostRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    // create
    public void save(Post entity) {
        String insertSql ="INSERT INTO post (content, like_count,username,created_at, updated_at) " +
                "VALUES (?,?,?,?,?) )";
        jdbcTemplate.update(insertSql,
                entity.getContent(),
                entity.getLikeCount(),
                entity.getUsername(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    // READ - 전체 조회
    public List<Post> findAll() {
        String selectSql = "SELECT " +
                "id, " +
                "content, " +
                "like_count AS likeCount, " +
                "username, " +
                "created_at AS createdAt, " +
                "updated_at AS updatedAt " +
                "FROM post";

        return jdbcTemplate.query(selectSql, new BeanPropertyRowMapper<>(Post.class));
    }


    public Optional<Post> findById(Long id) {
        String selectSql = "SELECT " +
                "id, " +
                "content, " +
                "like_count AS likeCount, " +
                "username, " +
                "created_at AS createdAt, " +
                "updated_at AS updatedAt " +
                "FROM post WHERE id = ?";

        try {
            Post post = jdbcTemplate.queryForObject(selectSql, new BeanPropertyRowMapper<>(Post.class), id);
            return Optional.of(post);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty(); // 조회 결과가 없으면 Optional.empty()
        }
    }


    // UPDATE
    public void update(Long id, Post post) {
        String updateSql = "UPDATE post SET content = ?, username = ?, updated_at = ? WHERE id = ?";
        jdbcTemplate.update(updateSql,
                post.getContent(),
                post.getUsername(),
                LocalDateTime.now(), // 엔티티에서 관리하는 updatedAt 사용
                id
        );
    }
    public void delete(Long id) {
        String deleteSql = "DELETE FROM post WHERE id = ?";
        jdbcTemplate.update(deleteSql, id);
    }

}