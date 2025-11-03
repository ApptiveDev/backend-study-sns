//DB에서 가져온 엔티티를 clinet에게 보낼 형식으로 만드는 DTO

package com.example.devSns.task.dto;

import com.example.devSns.task.Task;
import java.time.LocalDate;

public class TaskResponse { //외부에 노출할 필드만 담는 상자
    public Long id;
    public String title;
    public String description;
    public LocalDate dueDate;
    public Integer priority;
    public Task.Status status;

    public TaskResponse(Task t) { //복사
        this.id = t.getId();
        this.title = t.getTitle();
        this.description = t.getDescription();
        this.dueDate = t.getDueDate();
        this.priority = t.getPriority();
        this.status = t.getStatus();
    }
}
