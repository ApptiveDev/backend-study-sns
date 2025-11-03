//Task을 DB 테이블과 연결하는 엔티티

package com.example.devSns.task;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity                             //jpa 엔터티로 선언
@Table(name = "tasks")              //이름이 tasks 인 테이블 명시.
public class Task {

    public enum Status { TODO, IN_PROGRESS, DONE }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    protected Task() {} // JPA 기본생성자

    // getter/setter
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
