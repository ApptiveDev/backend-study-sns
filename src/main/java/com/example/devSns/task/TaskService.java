//컨트롤러와 DB 사이의 중간 관리자

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

    public Task create(TaskRequest r) {
        Task t = new Task();
        if (r.title != null) t.setTitle(r.title);
        t.setDescription(r.description);
        t.setDueDate(r.dueDate);
        t.setPriority(r.priority);
        t.setStatus(r.status == null ? Task.Status.TODO : r.status);
        return repo.save(t);
    }

    @Transactional(readOnly = true)
    public List<Task> findAll() { return repo.findAll(); }

    @Transactional(readOnly = true)
    public Task findById(Long id) { // 값이 없으면 TaskNotFound
        return repo.findById(id).orElseThrow(() -> new TaskNotFound(id));
    }

    public Task update(Long id, TaskRequest r) { //부분 수정 가능
        Task t = findById(id);
        if (r.title != null) t.setTitle(r.title);
        if (r.description != null) t.setDescription(r.description);
        if (r.dueDate != null) t.setDueDate(r.dueDate);
        if (r.priority != null) t.setPriority(r.priority);
        if (r.status != null) t.setStatus(r.status);
        return t; // JPA dirty checking
    }

    public void delete(Long id) { repo.delete(findById(id));  }
} // 존재하지 않으면 TaskNotFound
