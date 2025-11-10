package com.example.devSns.comment;

import com.example.devSns.member.Member;
import com.example.devSns.task.Task;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    private String username;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    // ★ 추가 부분
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Comment() {}

    public Comment(String content, String username, Task task, Member member) {
        this.content = content;
        this.username = username;
        this.task = task;
        this.member = member;
    }

    public Long getId() { return id; }
    public String getContent() { return content; }
    public String getUsername() { return username; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Task getTask() { return task; }
    public Member getMember() { return member; }
}
