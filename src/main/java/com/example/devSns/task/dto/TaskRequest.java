//client가 보낸 json 요청 받기 위한 dto

package com.example.devSns.task.dto;

import com.example.devSns.task.Task;
import java.time.LocalDate;

public class TaskRequest { // client 가 보낸 json 담는 곳
    public String title;
    public String description;
    public LocalDate dueDate;
    public Integer priority;
    public Task.Status status;
}
