package com.example.devSns.Comment;

import com.example.devSns.Comment.Dto.UpdateCommentDto;
import com.example.devSns.Post.Post;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="comment_id")
    private Long id;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="post_id")
    @JsonBackReference
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Comment> children;

    public Comment(String comment, String author) {
        this.comment = comment;
        this.author = author;
    }

    public void updateComment(UpdateCommentDto dto) {
        this.comment = dto.content();
        this.author = dto.author();
    }
    public void leaveReply(Comment reply) { // 댓글 남기기
        this.children.add(reply);
    }
    public void leavedParent(Comment parent) { // 부모 댓글 설정
        this.parent = parent;
        this.post = parent.getPost();
    }
    public void assignToPost(Post post) {
        this.post = post;
    }


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
