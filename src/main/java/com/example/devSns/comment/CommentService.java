package com.example.devSns.comment;

import com.example.devSns.comment.dto.CommentRequest;
import com.example.devSns.comment.dto.CommentResponse;
import com.example.devSns.task.Task;
import com.example.devSns.task.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepo;
    private final TaskRepository taskRepo;

    public CommentService(CommentRepository commentRepo, TaskRepository taskRepo) {
        this.commentRepo = commentRepo;
        this.taskRepo = taskRepo;
    }

    public CommentResponse create(Long taskId, CommentRequest req) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        Comment comment = new Comment(req.getContent(), req.getUsername(), task);
        Comment saved = commentRepo.save(comment);
        return new CommentResponse(saved);
    }

    public List<CommentResponse> findByTask(Long taskId) {
        return commentRepo.findByTaskId(taskId)
                .stream().map(CommentResponse::new).toList();
    }

    public void delete(Long commentId) {
        commentRepo.deleteById(commentId);
    }
}
