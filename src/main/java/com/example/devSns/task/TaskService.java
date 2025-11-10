package com.example.devSns.task;

import com.example.devSns.task.dto.TaskRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {
    private final TaskRepository repo;
    public TaskService(TaskRepository repo) { this.repo = repo; }

    // ✅ Task 생성
    public Task create(TaskRequest r) {
        Task t = new Task(r);
        return repo.save(t);
    }

    @Transactional(readOnly = true)
    public List<Task> findAll() { return repo.findAll(); }

    @Transactional(readOnly = true)
    public Task findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new TaskNotFound(id));
    }

    // ✅ Task 내부의 update()를 호출
    public Task update(Long id, TaskRequest r) {
        Task t = findById(id);
        t.update(r);
        return t;  // JPA dirty checking으로 자동 업데이트
    }

    public void delete(Long id) {
        repo.delete(findById(id));
    }
}
