package com.example.devSns.comment;

import com.example.devSns.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTaskId(Long taskId);

    List<Comment> findByMemberId(Long memberId);

    List<Comment> findByTask(Task task);
}
