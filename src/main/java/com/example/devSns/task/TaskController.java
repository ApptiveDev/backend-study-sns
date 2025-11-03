//HTTP 요청을 받아 서비스 호출과 응답 반환을 담당하는 API


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
    public TaskController(TaskService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody TaskRequest req) {
        Task saved = service.create(req);
        return ResponseEntity.created(URI.create("/api/tasks/" + saved.getId()))
                .body(new TaskResponse(saved));
    }

    @GetMapping
    public List<TaskResponse> list() {
        return service.findAll().stream().map(TaskResponse::new).toList();
    }

    @GetMapping("/{id}")
    public TaskResponse get(@PathVariable Long id) {
        return new TaskResponse(service.findById(id));
    }

    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable Long id, @RequestBody TaskRequest req) {
        return new TaskResponse(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
