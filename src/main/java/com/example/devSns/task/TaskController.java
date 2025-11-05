package com.example.devSns.task;

import com.example.devSns.task.dto.TaskRequest;
import com.example.devSns.task.dto.TaskResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody TaskRequest req) {
        Task saved = service.create(req);
        return ResponseEntity.created(URI.create("/api/tasks/" + saved.getId()))
                .body(new TaskResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> list() {
        List<TaskResponse> tasks = service.findAll().stream()
                .map(TaskResponse::new)
                .toList();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> get(@PathVariable Long id) {
        Task found = service.findById(id);
        return ResponseEntity.ok(new TaskResponse(found));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@PathVariable Long id, @RequestBody TaskRequest req) {
        Task updated = service.update(id, req);
        return ResponseEntity.ok(new TaskResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
