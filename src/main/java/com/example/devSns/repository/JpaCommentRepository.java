package com.example.devSns.repository;

import com.example.devSns.domain.Comment;
import com.example.devSns.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface JpaCommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findCommentsByCreatedAtBeforeAndPostId(LocalDateTime createdAt, Long postId);

    List<Comment> findTop15ByCreatedAtBeforeAndPostIdOrderByCreatedAtDescPostIdDesc(LocalDateTime createdAt, Long postId);

    @Modifying
    @Query("update Comment c set c.likeCount = c.likeCount + 1 where c.id = :id")
    void incrementLikeById(Long id);

    Long countCommentsByPostId(Long postId);

    @Query("select p.id, count(*) from Post p join Comment c on p = c.post where p in :posts group by p.id ")
    public List<Long[]> countCommentsAndGroupByPostIdIn(List<Post> posts);
}
