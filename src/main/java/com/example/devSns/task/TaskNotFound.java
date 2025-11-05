package com.example.devSns.task;

public class TaskNotFound extends RuntimeException {
    public TaskNotFound(Long id) { super("Task not found: " + id); }
}
