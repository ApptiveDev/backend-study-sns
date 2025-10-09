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

    //read 게시글 목록조회
    public List<Post> findAll() {
        String selectSql = "SELECT * FROM post";
        return jdbcTemplate.query(selectSql, new BeanPropertyRowMapper<>(Post.class));
    }
    //read 게시글 단일 조회
    public Post findById(Long id) {
        String selectSql = "SELECT * FROM post WHERE id = ?";
        return jdbcTemplate.queryForObject(selectSql, new BeanPropertyRowMapper<>(Post.class), id);
    }
    // 게시글 수정
    public void update(Long id, Post entity) {
        String updateSql ="UPDATE post SET content = ?, like_count = ?, updated_at = ? WHERE id = ?";
        jdbcTemplate.update(updateSql, entity.getContent(), entity.getLikeCount(), LocalDateTime.now(), id);
    }
    public void delete(Long id) {
        String deleteSql = "DELETE FROM post WHERE id = ?";
        jdbcTemplate.update(deleteSql, id);
    }

}