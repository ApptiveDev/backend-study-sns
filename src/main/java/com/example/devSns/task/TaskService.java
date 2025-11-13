package com.example.devSns.task;

import com.example.devSns.member.Member;
import com.example.devSns.member.MemberRepository;
import com.example.devSns.task.dto.TaskRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository repo;
    private final MemberRepository memberRepo;

    public TaskService(TaskRepository repo, MemberRepository memberRepo) {
        this.repo = repo;
        this.memberRepo = memberRepo;
    }

    public Task create(TaskRequest r) {
        Member member = memberRepo.findById(r.memberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Task t = new Task(r, member);
        return repo.save(t);
    }

    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Task findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new TaskNotFound(id));
    }

    @Transactional(readOnly = true)
    public List<Task> findByMember(Long memberId) {
        return repo.findByMemberId(memberId);
    }

    public Task update(Long id, TaskRequest r) {
        Task t = findById(id);
        t.update(r);
        return t;
    }

    public void delete(Long id) {
        repo.delete(findById(id));
    }
}
