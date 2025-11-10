package com.example.devSns.task;

import com.example.devSns.member.Member;
import com.example.devSns.task.dto.TaskRequest;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task {

    public enum Status { TODO, IN_PROGRESS, DONE }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    private LocalDate dueDate;
    private Integer priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.TODO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Task() {}

    // ✅ 생성자: TaskRequest + Member
    public Task(TaskRequest r, Member member) {
        this.title = r.title();
        this.description = r.description();
        this.dueDate = r.dueDate();
        this.priority = r.priority();
        this.status = (r.status() == null) ? Status.TODO : r.status();
        this.member = member;
    }

    // ✅ 부분 업데이트 메서드
    public void update(TaskRequest r) {
        if (r.title() != null) this.title = r.title();
        if (r.description() != null) this.description = r.description();
        if (r.dueDate() != null) this.dueDate = r.dueDate();
        if (r.priority() != null) this.priority = r.priority();
        if (r.status() != null) this.status = r.status();
    }

    // Getter
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDueDate() { return dueDate; }
    public Integer getPriority() { return priority; }
    public Status getStatus() { return status; }
    public Member getMember() { return member; }
}
