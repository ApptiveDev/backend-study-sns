package com.example.devSns.comment;

import com.example.devSns.comment.dto.CommentRequest;
import com.example.devSns.comment.dto.CommentResponse;
import com.example.devSns.task.Task;
import com.example.devSns.task.TaskRepository;
import com.example.devSns.member.Member;
import com.example.devSns.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepo;
    private final TaskRepository taskRepo;
    private final MemberRepository memberRepo;

    public CommentService(CommentRepository commentRepo, TaskRepository taskRepo, MemberRepository memberRepo) {
        this.commentRepo = commentRepo;
        this.taskRepo = taskRepo;
        this.memberRepo = memberRepo;
    }

    @Transactional
    public CommentResponse create(Long taskId, CommentRequest req) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        Member member = memberRepo.findById(req.memberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Comment comment = new Comment(req.content(), req.username(), task, member); // ★ 수정된 생성자 사용
        Comment saved = commentRepo.save(comment);

        return new CommentResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> findByTask(Long taskId) {
        return commentRepo.findByTaskId(taskId)
                .stream().map(CommentResponse::new).toList();
    }

    @Transactional
    public void delete(Long commentId) {
        commentRepo.deleteById(commentId);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> findByMember(Long memberId) {
        return commentRepo.findByMemberId(memberId)
                .stream()
                .map(CommentResponse::new)
                .toList();
    }

}
